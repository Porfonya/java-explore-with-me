package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.service.StatServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping()
public class StatsServerController {
    public final StatServiceImpl statService;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> createStat(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Создали новую статистику");
        return new ResponseEntity<>(statService.createStat(endpointHitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam(name = "start")
            String start,
            @RequestParam(name = "end")
            String end,
            @RequestParam(name = "uris", required = false) String[] uris,
            @RequestParam(name = "unique", defaultValue = "false", required = false)
            Boolean unique) {
        log.info("Получение статистики по посещениям");
        return new ResponseEntity<>(statService.getStats(LocalDateTime.parse(start,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), LocalDateTime.parse(end,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), uris, unique), HttpStatus.OK);
    }
}
