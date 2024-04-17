package ru.practicum.events;

import ru.practicum.events.dto.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventsService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEvent(User user, Long eventId);

    EventFullDto updateUserEventId(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<EventShortDto> getAllEventsFromUser(Long userId, int from, int size);

    EventFullDto updateAdminEventId(Long eventId, UpdateEventAdminRequest event);

    List<EventFullDto> getAllEventInformation(List<Long> users,
                                              List<String> states, List<Long> categories,
                                              String rangeStart, String rangeEnd, int from, int size);

    EventFullDto getEventId(Long eventId, HttpServletRequest request);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                     HttpServletRequest request);

    EventRequestStatusUpdateResult updateEventParticipants(Long userId, Long eventId,
                                                           EventRequestStatusUpdateRequest request);
}
