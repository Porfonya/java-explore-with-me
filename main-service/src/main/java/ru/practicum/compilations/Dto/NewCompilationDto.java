package ru.practicum.compilations.Dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Титул должен иметь от 1 до 50 символов")
    private String title;

}
