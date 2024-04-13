package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.categories.dto.CategoryDto;
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
    //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private String createdOn;
    private String description;

    //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    //   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private String eventDate;

    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;

    private int participantLimit;
    //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private String publishedOn;
    private Boolean requestModeration = true;
    private String state;

    private String title;
    private Long views;


}
