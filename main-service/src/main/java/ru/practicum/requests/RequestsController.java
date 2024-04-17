package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.checker.Checker;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping
public class RequestsController {
    public final RequestServiceImpl requestService;
    public final Checker checker;

    @PostMapping({"/users/{userId}/requests"})
    public ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        return new ResponseEntity<>(requestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping({"/users/{userId}/requests"})
    public ResponseEntity<List<ParticipationRequestDto>> getAllRequests(@PathVariable Long userId) {

        return new ResponseEntity<>(requestService.getAllRequests(userId), HttpStatus.OK);

    }

    @PatchMapping("/users/{userId}/requests/{requestsId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequest(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("Отмена своего запроса на участие в событии");
        return new ResponseEntity<>(requestService.updateRequestsCanceled(userId, requestsId), HttpStatus.OK);
    }
}
