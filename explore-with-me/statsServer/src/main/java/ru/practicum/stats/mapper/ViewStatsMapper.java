package ru.practicum.stats.mapper;

import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStatsDto mapToViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

}
