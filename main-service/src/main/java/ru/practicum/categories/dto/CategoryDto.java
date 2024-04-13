package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @Size(min = 1, max = 50, message = "Имя должен иметь от 1 до 50 символов")
    private String name;
}
