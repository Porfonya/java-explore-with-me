package ru.practicum.compilations;

import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationId(Long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);
}
