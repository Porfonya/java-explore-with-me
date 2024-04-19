package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;
import ru.practicum.events.EventsMapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventsMapper mapper;


    public CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents() != null ?
                        compilation.getEvents().stream()
                                .map(mapper::mapToEventsShortDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation mapToCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
