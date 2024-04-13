package ru.practicum.locations.dto;

import lombok.*;

import javax.persistence.*;


@Builder
@Setter
@Getter
public class LocationDto {
    private Float lat;
    private Float lon;
}
