package ru.practicum.requests;

import org.springframework.stereotype.Component;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapper {
    public RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(String.valueOf(request.getCreated()))
                .event(request.getEvent())
                .requester(request.getRequester())
                .status(String.valueOf(request.getStatus()))
                .build();
    }

    public List<RequestDto> mapToListRequestDto(Iterable<Request> requests) {
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(mapToRequestDto(request));
        }
        return requestDtos;
    }
    public ParticipationRequestDto mapToParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(String.valueOf(request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(String.valueOf(request.getStatus()))
                .build();
    }

}
