package ru.practicum.events;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.categories.CategoriesMapper;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.locations.LocationMapper;
import ru.practicum.requests.Request;
import ru.practicum.requests.RequestRepository;
import ru.practicum.users.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class EventsMapper {
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final CategoriesMapper categoriesMapper;
    private final RequestRepository requestRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event mapToEventFromNewEventDto(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                        formatter))
                .location(locationMapper.mapToLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .build();
    }

    public EventFullDto mapToFullEventDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(categoriesMapper.mapToCategory(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate() != null ? event.getEventDate().format(formatter) : null)
                .location(locationMapper.mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(countConfirmedRequests(requestRepository.findRequestsByEventId(event.getId())))
                .createdOn(event.getCreatedOn().format(formatter))
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(formatter) : null)
                .initiator(userMapper.mapToUserShortDto(event.getInitiator()))
                .state(State.valueOf(event.getState()))
                .views(event.getViews())
                .build();
    }

    public Long countConfirmedRequests(List<Request> requests) {


        return requests != null ? requests.stream()
                .filter(request -> request.getStatus() == Status.CONFIRMED).count() : 0L;
    }

    public List<EventFullDto> mapToListEventsFullDto(Iterable<Event> events) {
        List<EventFullDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(mapToFullEventDto(event));
        }
        return eventDtos;
    }

    public EventShortDto mapToEventsShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoriesMapper.mapToCategory(event.getCategory()))
                .confirmedRequests(countConfirmedRequests(requestRepository.findRequestsByEventId(event.getId())))
                .eventDate(event.getEventDate().format(formatter))
                .initiator(userMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> mapToListEventsShortDto(Iterable<Event> events) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event : events) {
            eventShortDtos.add(mapToEventsShortDto(event));
        }
        return eventShortDtos;
    }
}
