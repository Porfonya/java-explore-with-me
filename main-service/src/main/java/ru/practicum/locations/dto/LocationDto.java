package ru.practicum.locations.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Setter
@Getter
public class LocationDto {
    private Float lat;
    private Float lon;
}
