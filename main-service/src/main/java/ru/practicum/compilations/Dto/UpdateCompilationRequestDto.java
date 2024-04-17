package ru.practicum.compilations.Dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequestDto {

    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Заголовок подборки может быть от 1 до 50 символов")
    private String title;
}
