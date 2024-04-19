package ru.practicum.requests;

import org.springframework.stereotype.Component;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<ParticipationRequestDto> mapToListRequestDto(Iterable<Request> requests) {
        List<ParticipationRequestDto> requestDtos = new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(mapToParticipationRequestDto(request));
        }
        return requestDtos;
    }

    public ParticipationRequestDto mapToParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(formatter))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(String.valueOf(request.getStatus()))
                .build();
    }

}
