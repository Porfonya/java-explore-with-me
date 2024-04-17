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
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public ResponseEntity<EventFullDto> addEvent(@PathVariable @Valid @Positive Long userId,
                                                 @RequestBody @Validated NewEventDto eventDto) {
        log.info("Добавление нового события");
        return new ResponseEntity<>(eventsService.addEvent(userId, eventDto), HttpStatus.CREATED);
    }

    @GetMapping({"/users/{userId}/events"})
    public List<EventShortDto> getAllEventsFromUser(@PathVariable @Valid @Positive Long userId,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size) {

        return eventsService.getAllEventsFromUser(userId, from, size);
    }

    @GetMapping({"/users/{userId}/events/{eventId}"})
    public ResponseEntity<EventFullDto> getEventOfCurrentUser(
            @PathVariable(value = "userId") @Valid @Positive Long userId,
            @PathVariable(value = "eventId") @Valid @Positive Long eventId) {
        User user = checker.checkerAndReturnUser(userId);
        log.info("Получение полной информации о событии №{} текущего пользователя с id= {}", eventId, userId);
        return new ResponseEntity<>(eventsService.getEvent(user, eventId), HttpStatus.OK);


    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventOfCurrentUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Validated UpdateEventUserRequest eventDto) {

        log.info("Обновление информации о событии №{} текущего пользователя с id= {}", eventId, userId);
        return new ResponseEntity<>(eventsService.updateUserEventId(userId, eventId, eventDto), HttpStatus.OK);

    }


    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEventFullInformation(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = " states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") @Valid
            @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Valid @Positive int size) {
        return eventsService.getAllEventInformation(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateAdminEventId(@PathVariable Long eventId,
                                                           @RequestBody @Validated UpdateEventAdminRequest event) {

        return new ResponseEntity<>(eventsService.updateAdminEventId(eventId, event), HttpStatus.OK);
    }

    @GetMapping("/events")
    public List<EventShortDto> getPublishedEvents(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(value = "from", defaultValue = "0")
                                                  @Valid @PositiveOrZero int from,
                                                  @RequestParam(value = "size", defaultValue = "10")
                                                  @Valid @Positive int size,
                                                  HttpServletRequest request) {

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventsService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> getEventId(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {

        return new ResponseEntity<>(eventsService.getEventId(eventId, request), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(@PathVariable Long userId,
                                                                              @PathVariable Long eventId) {
        return new ResponseEntity<>(eventsService.getEventParticipants(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventParticipants(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest request) {
        return eventsService.updateEventParticipants(userId, eventId, request);
    }
}
