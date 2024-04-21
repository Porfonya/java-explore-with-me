package ru.practicum.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCommentDto {
    @NotBlank
    @NotNull
    @Size(min = 6, max = 1000, message = "Комментарий  должен иметь от 6 до 1000 символов")
    private String text;
}
