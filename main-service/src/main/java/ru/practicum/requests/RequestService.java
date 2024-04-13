package ru.practicum.requests;

import org.springframework.http.ResponseEntity;
import ru.practicum.events.dto.EventDto;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<RequestDto> getAllRequests(Long userId);

    RequestDto updateRequestsCanceled(Long userId, Long requestsId);
}
