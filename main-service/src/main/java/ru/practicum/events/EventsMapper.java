package ru.practicum.events;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.categories.CategoriesMapper;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.locations.LocationMapper;
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

    public Event mapToEventFromNewEventDto(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(locationMapper.mapToLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .build();
    }

    public EventFullDto mapToFullEventDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoriesMapper.mapToCategory(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(String.valueOf(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(String.valueOf(event.getEventDate()))
                .initiator(userMapper.mapToUserShortDto(event.getInitiator()))
                .location(locationMapper.mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(String.valueOf(event.getEventDate()))
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(event.getViews())
                .build();
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
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(String.valueOf(event.getEventDate()))
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
    public EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(String.valueOf(event.getEventDate()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .build();
    }
    public List<EventDto> mapToListEventsDto(Iterable<Event> events) {
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(mapToEventDto(event));
        }
        return eventDtos;
    }


  /*   public EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(String.valueOf(event.getEventDate()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .build();
    }


    public Event mapToEvent(EventDto eventDto) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(LocalDateTime.parse(eventDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .build();
    }

    public static Event mapToEventFromNewEventDto(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .build();
    }


   *//*
    }


    public Event mapToFullEventDto(EventFullDto eventFullDto) {
        return Event.builder()
                .annotation(eventFullDto.getAnnotation())
                .category(eventFullDto.getCategory())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .createdOn(LocalDateTime.parse(eventFullDto.getCreatedOn(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(eventFullDto.getDescription())
                .eventDate(LocalDateTime.parse(eventFullDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(eventFullDto.getInitiator())
                .location(eventFullDto.getLocation())
                .paid(eventFullDto.getPaid())
                .participantLimit(eventFullDto.getParticipantLimit())
                .publishedOn(LocalDateTime.parse(eventFullDto.getPublishedOn(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .requestModeration(eventFullDto.getRequestModeration())
                .title(eventFullDto.getTitle())
                .state(eventFullDto.getState())
                .views(eventFullDto.getViews())
                .build();
    }
*//*
    public List<EventDto> mapToListEventsDto(Iterable<Event> events) {
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(mapToEventDto(event));
        }
        return eventDtos;
    }


*/

}
