package ru.practicum.compilations.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    @NotNull
    @Size(min = 1, max = 50, message = "Титул не более 50 символов")
    private String title;

}
