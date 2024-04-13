package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

@Setter
@Getter
@Builder
public class RequestDto {
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private String created;
    private Event event;
    private Long id;
    private User requester;
    private String status;

}
