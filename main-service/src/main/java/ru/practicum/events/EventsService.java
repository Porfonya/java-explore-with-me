package ru.practicum.events;

import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;

import java.util.Collection;
import java.util.List;

public interface EventsService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventDto> getAllEvents(int from, int size);
    EventFullDto getEvent(User user, Long eventId);

    EventFullDto updateUserEventId(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<EventShortDto> getAllEventsFromUser(Long userId, int from, int size);

    EventFullDto updateAdminEventId(Long eventId, UpdateEventAdminRequest event);

    List<EventFullDto> getAllEventFullInformation(Collection<Long> users,
                                           Collection<String> states, Collection<Long> categories,
                                           String rangeStart, String rangeEnd, int from, int size);

    EventFullDto getEventId(Long eventId);

    ParticipationRequestDto getEventParticipants(Long userId, Long eventId);
}
