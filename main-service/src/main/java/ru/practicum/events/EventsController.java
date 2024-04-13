package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.checker.Checker;
import ru.practicum.events.dto.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping
public class EventsController {
    public final EventsServiceImpl eventsService;
    public final UserRepository userRepository;
    public final Checker checker;

    @PostMapping({"/users/{userId}/events"})
    public ResponseEntity<EventFullDto> addEvent(@PathVariable(value = "userId") Long userId,
                                                 @RequestBody @Validated NewEventDto eventDto) {
        log.info("Добавление нового события");
        return new ResponseEntity<>(eventsService.addEvent(userId, eventDto), HttpStatus.CREATED);
    }

    @GetMapping({"/users/{userId}/events"})
    public List<EventShortDto> getAllEventsFromUser(@PathVariable(value = "userId") Long userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {

        return eventsService.getAllEventsFromUser(userId, from, size);
    }

    @GetMapping({"/users/{userId}/events/{eventId}"})
    public ResponseEntity<EventFullDto> getEventOfCurrentUser(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "eventId") Long eventId) {
        User user = checker.checkerAndReturnUser(userId);
        log.info("Получение полной информации о событии №{} текущего пользователя с id= {}", eventId, userId);
        return new ResponseEntity<>(eventsService.getEvent(user, eventId), HttpStatus.OK);


    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventOfCurrentUser(
            @PathVariable Long userId,
            @PathVariable(required = false) Long eventId,
            @RequestBody @Validated UpdateEventUserRequest eventDto) {

        log.info("Обновление информации о событии №{} текущего пользователя с id= {}", eventId, userId);
        return new ResponseEntity<>(eventsService.updateUserEventId(userId, eventId, eventDto), HttpStatus.OK);

    }

    @GetMapping("/events")
    public List<EventDto> getAllEvents(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return eventsService.getAllEvents(from, size);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEventFullInformation(@RequestParam Collection<Long> users,
                                                         @RequestParam Collection<String> states,
                                                         @RequestParam Collection<Long> categories,
                                                         @RequestParam String rangeStart,
                                                         @RequestParam String rangeEnd,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return eventsService.getAllEventFullInformation(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateAdminEventId(@PathVariable Long eventId,
                                           @RequestBody @Validated UpdateEventAdminRequest event) {

        return new ResponseEntity<>(eventsService.updateAdminEventId(eventId, event), HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> getEventId(@PathVariable(name = "id") Long eventId) {

        return new ResponseEntity<>(eventsService.getEventId(eventId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return new ResponseEntity<>(eventsService.getEventParticipants(userId, eventId), HttpStatus.OK);
    }
    @GetMapping("/some/path/{id}")
    public void logIPAndPath(@PathVariable long id, HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
    }
}
