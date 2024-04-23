package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.users.dto.UserShortDto;

@Setter
@Getter
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;

}
