package ru.practicum.users.dto;

import lombok.*;

@Setter
@Getter
@Builder
public class UserShortDto {
    private Long id;
    private String name;

}
