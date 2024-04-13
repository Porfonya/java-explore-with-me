package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.checker.Checker;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.events.model.Event;
import ru.practicum.exception.ConflictExc;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.users.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final Checker checker;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = checker.checkerAndReturnUser(userId);
        Event event = checker.checkerAndReturnEvent(eventId);


        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new ConflictExc("Нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictExc("Инициатор события не может добавить запрос на участие в своём событии ");
        }
        if (!event.getState().equals(String.valueOf(State.PUBLISHED))) {
            throw new ConflictExc("Нельзя участвовать в неопубликованном событии");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(Status.CONFIRMED)
                .build();

        return requestMapper.mapToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getAllRequests(Long userId) {

        return requestMapper.mapToListRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestDto updateRequestsCanceled(Long userId, Long requestsId) {
        checker.checkerUser(userId);
        checker.checkerRequest(requestsId);
        Request request = requestRepository.findRequestByIdAndRequesterId(requestsId, userId);
        request.setStatus(Status.REJECTED);
        return requestMapper.mapToRequestDto(request);
    }
}
