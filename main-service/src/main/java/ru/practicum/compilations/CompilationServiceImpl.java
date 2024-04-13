package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.Dto.CompilationDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.compilations.Dto.NewCompilationDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j

public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper mapper;
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.mapToNewCompilationDto(newCompilationDto);
        compilationRepository.save(compilation);

        return mapper.mapToCompilation(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Compilation c  id = {} удален", compId);
    }

    @Override
    public CompilationDto getAllCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        if (pinned) {

        } else {

        }

        return null;
    }

    @Override
    public CompilationDto getCompilationId(Long compId) {
        return null;
    }


}
