package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventsMapper;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventRepository eventRepository;
    private final EventsMapper mapper;

    public Compilation mapToNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .events(convert(newCompilationDto.getEvents()))
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned()).build();
    }

    public CompilationDto mapToCompilation(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(convertToEventShortDto(compilation.getEvents()))
                .title(compilation.getTitle())
                .pinned(compilation.getPinned()).build();
    }

    private Set<Event> convert(Set<Long> eventId) {
        Set<Event> result = new HashSet<>();
        for (Long value : eventId) {
            result.add(eventRepository.getReferenceById(value));
        }
        return result;
    }

    private List<EventShortDto> convertToEventShortDto(Set<Event> event) {
        List<EventShortDto> result = new ArrayList<>();
        for (Event value : event) {
            Event e = eventRepository.getReferenceById(value.getId());
            result.add(mapper.mapToEventsShortDto(e));
        }
        return result;
    }

}
