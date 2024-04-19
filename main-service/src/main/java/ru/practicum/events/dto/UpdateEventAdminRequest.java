package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.enums.StateAdminAction;
import ru.practicum.locations.Location;

import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Аннотация должна иметь от 20 до 2000 символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Описание должен иметь от 20 до 7000 символов")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAdminAction stateAction;
    @Size(min = 3, max = 120, message = "Титул должен иметь от 3 до 120 символов")
    private String title;

}
