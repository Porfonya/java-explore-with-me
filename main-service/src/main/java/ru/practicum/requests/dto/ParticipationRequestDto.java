package ru.practicum.requests.dto;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
@Setter
@Getter
@RequiredArgsConstructor
@Builder
public class ParticipationRequestDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;

    public ParticipationRequestDto(String created, Long event, Long id, Long requester, String status) {
        this.created = created;
        this.event = event;
        this.id = id;
        this.requester = requester;
        this.status = status;
    }
}
