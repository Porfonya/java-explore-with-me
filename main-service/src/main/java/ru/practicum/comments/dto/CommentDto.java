package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @Size(min = 1, max = 50, message = "Имя должен иметь от 1 до 50 символов")
    private String text;
    private String stateComment;
    private String created;
    private String updated;
    private Long eventId;
    private Long authorId;
}
