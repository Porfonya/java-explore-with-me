package ru.practicum.compilations;

import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;
import ru.practicum.compilations.Dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationId(Long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto compilationDto);
}
