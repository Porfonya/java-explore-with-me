package ru.practicum.locations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.locations.dto.LocationDto;

@AllArgsConstructor
@Component
public class LocationMapper {
    public LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public Location mapToLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
