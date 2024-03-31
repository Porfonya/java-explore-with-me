package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.exception.ValidationException;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.mapper.ViewStatsMapper;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.ViewStats;
import ru.practicum.stats.repository.ViewStatsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final ViewStatsRepository statsRepository;


    @Override
    public EndpointHitDto createStat(EndpointHitDto endpointHitDto) {

        EndpointHit endpointHit = EndpointHitMapper.mapToEndpointHit(endpointHitDto);
        return EndpointHitMapper.mapToEndpointHitDto(statsRepository.save(endpointHit));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<ViewStats> viewStats;

        if (start.isAfter(end)) {
            throw new ValidationException("Date start must be before date end");
        }
        if (unique) {
            if (uris == null) {
                viewStats = statsRepository.findAllUrisWithUniqueIp(start, end);

            } else {
                viewStats = statsRepository.findUrisWithUniqueIp(uris, start, end);
            }
        } else {
            if (uris == null) {
                viewStats = statsRepository.findAllUris(start, end);

            } else {

                viewStats = statsRepository.findUris(uris, start, end);
            }
        }
        return !viewStats.isEmpty() ? viewStats.stream()
                .map(ViewStatsMapper::mapToViewStatsDto)
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}
