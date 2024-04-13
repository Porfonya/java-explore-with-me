package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.checker.Checker;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAdminAction;
import ru.practicum.enums.StateUserAction;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.ValidationExc;
import ru.practicum.locations.LocationMapper;
import ru.practicum.locations.LocationRepository;
import ru.practicum.requests.Request;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

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
        log.info("Добавили новое событие");
        Event resultEvent = eventRepository.save(event);
        log.info("Сохранили новое событие");
        return mapper.mapToFullEventDto(resultEvent);
    }

    @Override
    public List<EventShortDto> getAllEventsFromUser(Long userId, int from, int size) {
        checker.checkerUser(userId);

        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, page);

        return mapper.mapToListEventsShortDto(events);
    }

    @Override
    public EventFullDto updateAdminEventId(Long eventId, UpdateEventAdminRequest event) {
        Event existEvent = checker.checkerAndReturnEvent(eventId);
        if (existEvent != null) {

         //   existEvent.setAnnotation(event.getAnnotation());

            existEvent.setCategory(event.getCategory() == null ? existEvent.getCategory() :
                    categoriesRepository.getReferenceById(event.getCategory()));

        //    existEvent.setDescription(event.getDescription());

            existEvent.setEventDate(event.getEventDate() == null ? existEvent.getEventDate() :
                    event.getEventDate());
            existEvent.setLocation(event.getLocation() == null ? existEvent.getLocation() : event.getLocation());
            existEvent.setPaid(event.getPaid() == null ? existEvent.getPaid() : event.getPaid());

            existEvent.setParticipantLimit(event.getParticipantLimit() == 0 ? existEvent.getParticipantLimit()
                    : event.getParticipantLimit());


            existEvent.setRequestModeration(event.getRequestModeration() == null ? existEvent.getRequestModeration()
                    : event.getRequestModeration());
            existEvent.setState(event.getStateAction() == null ? existEvent.getState() : event.getStateAction());

            if (event.getStateAction() == null) {
                existEvent.setState(existEvent.getState());
            }
            if (event.getStateAction().equals(String.valueOf(StateAdminAction.REJECT_EVENT))) {
                existEvent.setState(String.valueOf(State.CANCELED));
            }
            if (event.getStateAction().equals(String.valueOf(StateAdminAction.PUBLISH_EVENT))) {
                existEvent.setState(String.valueOf(State.PUBLISHED));
            }
        //    existEvent.setTitle(event.getTitle());

            return mapper.mapToFullEventDto(eventRepository.save(existEvent));
        } else {
            throw new EventNotFoundException(eventId);
        }
    }


    @Override
    public List<EventDto> getAllEvents(int from, int size) {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            System.out.println(event);
        }
        return mapper.mapToListEventsDto(events);
    }

    @Override
    public EventFullDto getEvent(User user, Long eventId) {

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user);
        return mapper.mapToFullEventDto(event);
    }

    @Override
    public EventFullDto updateUserEventId(Long userId, Long eventId, UpdateEventUserRequest event) {

        checker.checkerUser(userId);
        Event existEvent = checker.checkerAndReturnEvent(eventId);
        log.info("создали событие" + existEvent);

        if (existEvent.getState() != null && (!existEvent.getState().equals(String.valueOf(State.CANCELED))
                && !existEvent.getState().equals(String.valueOf(State.PENDING)))) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (event.getEventDate() != null
                && LocalDateTime.parse(event.getEventDate(), formatter)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationExc("Event must not be published");
        }
       // existEvent.setId(eventId);
        existEvent.setAnnotation(event.getAnnotation());
        existEvent.setCategory(event.getCategory() == null ? existEvent.getCategory() :
                categoriesRepository.getReferenceById(event.getCategory()));

       // existEvent.setConfirmedRequests(existEvent.getConfirmedRequests());
       // existEvent.setDescription(event.getDescription());

        existEvent.setEventDate(event.getEventDate() == null ? existEvent.getEventDate() :
                LocalDateTime.parse(event.getEventDate(), formatter));
        existEvent.setLocation(event.getLocation() == null ? existEvent.getLocation()
                : locationMapper.mapToLocation(event.getLocation()));

        existEvent.setPaid(event.getPaid() == null ? existEvent.getPaid() : event.getPaid());

        existEvent.setParticipantLimit(event.getParticipantLimit() == 0 ? existEvent.getParticipantLimit()
                : event.getParticipantLimit());
        existEvent.setRequestModeration(event.getRequestModeration() == null ? existEvent.getRequestModeration()
                : event.getRequestModeration());
        if (event.getStateAction() == null) {
            existEvent.setState(existEvent.getState());
        }
        if (event.getStateAction().equals(String.valueOf(StateUserAction.CANCEL_REVIEW))) {
            existEvent.setState(String.valueOf(State.CANCELED));
        }
        if (event.getStateAction().equals(String.valueOf(StateUserAction.SEND_TO_REVIEW))) {
            existEvent.setState(String.valueOf(State.PENDING));
        }
      //  existEvent.setTitle(event.getTitle());

        return mapper.mapToFullEventDto(eventRepository.save(existEvent));
    }

    @Override
    public List<EventFullDto> getAllEventFullInformation(Collection<Long> users,
                                                         Collection<String> states, Collection<Long> categories,
                                                         String rangeStart, String rangeEnd, int from, int size) {
        validateStates(states);
        Pageable page = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findEvents(users, states, categories,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), page);


        return mapper.mapToListEventsFullDto(events);
    }

    @Override
    public EventFullDto getEventId(Long eventId) {
        checker.checkerEvent(eventId);

        Event event = eventRepository.getReferenceById(eventId);
        if (event.getPublishedOn() != null) {
            return mapper.mapToFullEventDto(event);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    private void validateStates(Collection<String> states) {
        if (states != null) {
            for (String state : states)
                try {
                    State.valueOf(state);
                } catch (IllegalArgumentException e) {
                    throw new ValidationExc("Wrong states!");
                }
        }
    }

    public ParticipationRequestDto getEventParticipants(Long userId, Long eventId) {
        checker.checkerUser(userId);
        checker.checkerEvent(eventId);
        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        return requestMapper.mapToParticipationRequestDto(request);

    }

   /*

    @Override
    public EventFullDto getEvent(User user, Long eventId) {
        Event event = eventRepository.findEventByIdAndInitiator(eventId, user);
        return mapper.mapToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsFromUser(Long userId, int from, int size) {
        checker.checkerUser(userId);
        Pageable page = PageRequest.of(from / size, size);
        Page<Event> events = eventRepository.findByInitiatorId(userId, page);

        return mapper.mapToListEventsShortDto(events);
    }*/


}
