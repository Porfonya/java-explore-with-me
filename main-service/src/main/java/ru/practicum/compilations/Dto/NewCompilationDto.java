package ru.practicum.compilations.Dto;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class NewCompilationDto {
    @UniqueElements(message = "events: uniqueItems")
    private Set<Long> events;
    private Boolean pinned = false;
    @Size(min = 1, max = 50, message = "Титул должен иметь от 1 до 50 символов")
    private String title;

}
