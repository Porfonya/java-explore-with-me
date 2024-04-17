package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.Dto.CompilationDto;
import ru.practicum.compilations.Dto.NewCompilationDto;
import ru.practicum.compilations.Dto.UpdateCompilationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping()
public class CompilationController {

    public final CompilationServiceImpl compilationService;


    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Поиск всех подборок");
        return compilationService.getAllCompilations(pinned, from, size);
    }


    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationsId(@PathVariable @Valid @Positive Long compId) {

        return new ResponseEntity<>(compilationService.getCompilationId(compId), HttpStatus.OK);
    }

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> addCompilation(
            @RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("Добавление новых подборок");
        return new ResponseEntity<>(compilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequestDto compilationDto) {
        log.info("Обновление Compilation c id = {}", compId);
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilationsId(@PathVariable Long compId) {
        log.info("Удаляем Compilation с id = {}", compId);
        compilationService.deleteCompilation(compId);

    }


}
