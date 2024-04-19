package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.checker.Checker;
import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;
import ru.practicum.compilations.Dto.UpdateCompilationRequestDto;
import ru.practicum.events.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper mapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final Checker checker;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);

        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }
        return !compilations.isEmpty() ? compilations.stream().map(mapper::mapToCompilationDto)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public CompilationDto getCompilationId(Long compId) {
        return mapper.mapToCompilationDto(compilationRepository.findById(compId).orElseThrow(()
                -> new NotFoundException(compId)));
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.mapToCompilation(newCompilationDto);

        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findByIdIn(newCompilationDto.getEvents()));
        }
        return mapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto compilationDto) {
        Compilation compilation = checker.checkerAndReturnCompilation(compId);

        compilation.setTitle(compilationDto.getTitle());
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findByIdIn(compilationDto.getEvents()));
        }
        return mapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        checker.checkerCompilation(compId);
        log.info("Compilation c  id = {} удален", compId);
        compilationRepository.deleteById(compId);

    }


}
