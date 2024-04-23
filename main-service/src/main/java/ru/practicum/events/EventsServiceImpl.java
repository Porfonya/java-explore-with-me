package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.checker.Checker;
import ru.practicum.enums.EventsSort;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.exception.ConflictExc;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationExc;
import ru.practicum.locations.Location;
import ru.practicum.locations.LocationMapper;
import ru.practicum.locations.LocationRepository;
import ru.practicum.requests.Request;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.service.StatClientService;
import ru.practicum.users.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventsServiceImpl implements EventsService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;
    private final EventsMapper mapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;
    private final Checker checker;
    private final StatClientService statClientService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = checker.checkerAndReturnUser(userId);
        Category category = checker.checkerAndReturnCategory(newEventDto.getCategory());

        Event event = mapper.mapToEventFromNewEventDto(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationExc("Неправильная дата");
        }
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setState(String.valueOf(State.PENDING));
        event.setLocation(locationRepository.save(locationMapper.mapToLocation(newEventDto.getLocation())));
        event.setViews(0L);
        Event resultEvent = eventRepository.save(event);

        return mapper.mapToFullEventDto(resultEvent);
    }

    @Override
    public List<EventShortDto> getAllEventsFromUser(Long userId, int from, int size) {
        checker.checkerUser(userId);
        Pageable page = PageRequest.of(from / size, size);
        Page<Event> events = eventRepository.findByInitiatorId(userId, page);

        return mapper.mapToListEventsShortDto(events);
    }

    @Override
    public EventFullDto updateAdminEventId(Long eventId, UpdateEventAdminRequest eventAdminRequest) {
        Event existEvent = checker.checkerAndReturnEvent(eventId);
        if (eventAdminRequest.getStateAction() != null) {
            switch (eventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT: {
                    if (!existEvent.getState().equals(String.valueOf(State.PENDING))) {
                        throw new ConflictExc("нельзя опубликовать, статус:" + existEvent.getState());
                    }
                    if (existEvent.getPublishedOn() != null && existEvent.getEventDate()
                            .isAfter(existEvent.getPublishedOn().minusHours(1))) {
                        throw new ValidationExc("Нельзя опубликовать, дата и время не правильные");
                    }
                    existEvent.setPublishedOn(LocalDateTime.now());
                    existEvent.setState(String.valueOf(State.PUBLISHED));
                    break;
                }
                case REJECT_EVENT: {
                    if (existEvent.getState().equals(String.valueOf(State.PUBLISHED))) {
                        throw new ConflictExc("Уже опубликовано, отменить нельзя");
                    } else {
                        existEvent.setState(String.valueOf(State.CANCELED));
                    }
                    break;
                }
            }
        }

        if (eventAdminRequest.getEventDate() != null
                && LocalDateTime.parse(eventAdminRequest.getEventDate(), formatter)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationExc("Дата и время на которые намечено событие введены не правильно");
        }
        if (eventAdminRequest.getTitle() != null) {
            existEvent.setTitle(eventAdminRequest.getTitle());
        }
        if (eventAdminRequest.getAnnotation() != null) {
            existEvent.setAnnotation(eventAdminRequest.getAnnotation());
        }
        if (eventAdminRequest.getCategory() != null) {
            existEvent.setCategory(categoriesRepository.findById(eventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(eventAdminRequest.getCategory())));
        }
        if (eventAdminRequest.getDescription() != null) {
            existEvent.setDescription(eventAdminRequest.getDescription());
        }
        if (eventAdminRequest.getEventDate() != null) {
            existEvent.setEventDate(LocalDateTime.parse(eventAdminRequest.getEventDate(), formatter));
        }
        if (eventAdminRequest.getLocation() != null) {
            Location location = existEvent.getLocation();
            location.setLat(eventAdminRequest.getLocation().getLat());
            location.setLon(eventAdminRequest.getLocation().getLon());
            existEvent.setLocation(location);
            locationRepository.save(location);
        }
        if (eventAdminRequest.getPaid() != null) {
            existEvent.setPaid(eventAdminRequest.getPaid());
        }
        if (eventAdminRequest.getParticipantLimit() != null) {
            existEvent.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }
        if (eventAdminRequest.getRequestModeration() != null) {
            existEvent.setRequestModeration(eventAdminRequest.getRequestModeration());
        }
        if (eventAdminRequest.getParticipantLimit() != null) {
            existEvent.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }
        return mapper.mapToFullEventDto(eventRepository.save(existEvent));
    }


    @Override
    public EventFullDto getEvent(User user, Long eventId) {

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user);
        return mapper.mapToFullEventDto(event);
    }

    @Override
    public EventFullDto updateUserEventId(Long userId, Long eventId, UpdateEventUserRequest eventUpdate) {

        checker.checkerUser(userId);
        Event existEvent = checker.checkerAndReturnEvent(eventId);
        log.info("создали событие" + existEvent);

        if (existEvent.getState() != null && (!existEvent.getState().equals(String.valueOf(State.CANCELED))
                && !existEvent.getState().equals(String.valueOf(State.PENDING)))) {
            throw new ConflictExc("Могут быть изменены только CANCELED и PENDING");
        }
        if (eventUpdate.getEventDate() != null
                && LocalDateTime.parse(eventUpdate.getEventDate(), formatter)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationExc("Событие не может быть опубликовано");
        }

        if (eventUpdate.getTitle() != null) {
            existEvent.setTitle(eventUpdate.getTitle());
        }
        if (eventUpdate.getAnnotation() != null) {
            existEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getCategory() != null) {
            existEvent.setCategory(categoriesRepository.findById(eventUpdate.getCategory()).orElseThrow(() ->
                    new NotFoundException(eventUpdate.getCategory())));
        }

        if (eventUpdate.getDescription() != null) {
            existEvent.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getEventDate() != null) {

            existEvent.setEventDate(LocalDateTime.parse(eventUpdate.getEventDate(), formatter));
        }
        if (eventUpdate.getLocation() != null) {
            Location location = existEvent.getLocation();
            location.setLat(eventUpdate.getLocation().getLat());
            location.setLon(eventUpdate.getLocation().getLon());
            existEvent.setLocation(location);
            locationRepository.save(location);
        }
        if (eventUpdate.getPaid() != null) {
            existEvent.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            existEvent.setParticipantLimit(eventUpdate.getParticipantLimit());
        }
        if (eventUpdate.getRequestModeration() != null) {

            existEvent.setRequestModeration(eventUpdate.getRequestModeration());
        }

        if (eventUpdate.getStateAction() != null) {
            switch (eventUpdate.getStateAction()) {
                case CANCEL_REVIEW:
                    existEvent.setState(String.valueOf(State.CANCELED));
                    break;
                case SEND_TO_REVIEW:
                    existEvent.setState(String.valueOf(State.PENDING));
                    break;
                default:
                    throw new ConflictExc("ASH");
            }
        }

        Event event = eventRepository.save(existEvent);
        return mapper.mapToFullEventDto(event);
    }

    @Override
    public List<EventFullDto> getAllEventInformation(List<Long> users, List<String> states, List<Long> categories,
                                                     String rangeStart, String rangeEnd, int from, int size) {
        List<Event> events = eventRepository.findEvents(users,
                checker.validateEventStates(states), categories,
                rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null,
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null,
                PageRequest.of(from / size, size));
        return mapper.mapToListEventsFullDto(events);
    }


    @Override
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                            HttpServletRequest request) {
        log.info("Client ip: {}", request.getRemoteAddr());
        log.info("Endpoint path: {}", request.getRequestURI());
        statClientService.post(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        if (rangeStart != null && rangeEnd != null &&
                LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
            throw new ValidationExc("Date start is after date end.");
        }
        List<Event> events = eventRepository.findPublishedEvents(
                text,
                categories,
                paid,
                rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now(),
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null,
                PageRequest.of(from / size, size));
        List<EventShortDto> eventShortDtos = Collections.emptyList();

        if (events != null) {
            eventShortDtos = mapper.mapToListEventsShortDto(events);
            if (onlyAvailable) {
                for (EventShortDto shortDto : eventShortDtos) {
                    for (Event event : events) {
                        if (shortDto.getConfirmedRequests() < event.getParticipantLimit()) {
                            eventShortDtos.add(shortDto);
                        }
                    }
                }
            }
        }
        if (sort != null) {
            switch (EventsSort.valueOf(sort)) {
                case EVENT_DATE:
                    eventShortDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
                    break;
                case VIEWS:
                    eventShortDtos.sort(Comparator.comparing(EventShortDto::getViews));
                    break;
                default:
                    throw new ValidationExc("Неправльные выходные данные");
            }
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventId(Long eventId, HttpServletRequest request) {
        checker.checkerEvent(eventId);
        Event event = eventRepository.findByIdAndState(eventId, String.valueOf(State.PUBLISHED))
                .orElseThrow(() -> new NotFoundException(eventId));
        Long countHits = getCountHits(request);


        statClientService.post(EndpointHitDto.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        Long newCountHits = getCountHits(request);
        if (newCountHits != null && newCountHits > countHits) {
            event.setViews(event.getViews() + 1L);
            eventRepository.save(event);
        }
        return mapper.mapToFullEventDto(event);
    }

    private Long getCountHits(HttpServletRequest request) {
        ResponseEntity<ViewStatsDto[]> response = statClientService.getStats(
                LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().format(formatter),
                new String[]{request.getRequestURI()},
                true);
        Optional<ViewStatsDto> viewStatsDto;
        Long hits = 0L;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            viewStatsDto = Arrays.stream(response.getBody()).findFirst();
            if (viewStatsDto.isPresent()) {
                hits = viewStatsDto.get().getHits();
            }
        }
        return hits;
    }

    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        User user = checker.checkerAndReturnUser(userId);
        checker.checkerEvent(eventId);
        List<Request> request = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, user.getId());
        return requestMapper.mapToListRequestDto(request);

    }

    @Override
    public EventRequestStatusUpdateResult updateEventParticipants(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateRequest request) {
        User user = checker.checkerAndReturnUser(userId);
        checker.checkerEvent(eventId);
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user);
        List<Request> requests = requestRepository.findRequestsByEventId(eventId);
        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
            event.setState(String.valueOf(Status.CONFIRMED));
        }
        if (event.getParticipantLimit() <= mapper.countConfirmedRequests(requests)) {
            throw new ConflictExc("Достигнут лимит по заявкам");
        }

        for (Long requestId : request.getRequestIds()) {
            for (Request req : requests) {
                if (req.getId().equals(requestId)) {
                    if (!req.getStatus().equals(Status.PENDING)) {
                        throw new ConflictExc("Статус можно изменить только у заявок, находящихся в состоянии ожидания ");
                    } else {
                        req.setStatus(Status.valueOf(request.getStatus()));
                        if (event.getParticipantLimit() < mapper.countConfirmedRequests(requests)) {
                            if (req.getStatus().equals(Status.PENDING)) {
                                req.setStatus(Status.REJECTED);
                            }
                        }
                    }
                }
            }
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request value : requests) {
            if (value.getStatus().equals(Status.CONFIRMED)) {
                confirmedRequests.add(requestMapper.mapToParticipationRequestDto(value));
            }
            if (value.getStatus().equals(Status.REJECTED)) {
                rejectedRequests.add(requestMapper.mapToParticipationRequestDto(value));
            }

        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();

    }


}
