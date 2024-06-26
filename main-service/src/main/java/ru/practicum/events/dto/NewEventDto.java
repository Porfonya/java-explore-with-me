package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.locations.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @PositiveOrZero
    private Long category;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private LocationDto location;
    private boolean paid = false;
    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
}
