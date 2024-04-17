package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class NewCategoryDto {
    @Size(min = 1, max = 50, message = "Имя должен иметь от 1 до 50 символов")
    @NotNull(message = "Name должно быть оюязательным полем")
    @NotBlank(message = "Нельзя добавлять категории с именем, состоящим из пробелов")
    private String name;
}
