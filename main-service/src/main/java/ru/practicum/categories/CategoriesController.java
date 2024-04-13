package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping()
public class CategoriesController {
    public final CategoriesServiceImpl categoriesService;


    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Validated NewCategoryDto categoryDto) {
        log.info("Категория добавлена");
        return new ResponseEntity<>(categoriesService.addCategory(categoryDto), HttpStatus.CREATED);
    }

    @PatchMapping({"/admin/categories/{catId}"})
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("Обновление категории с id={}", catId);
        return categoriesService.updateCategory(catId, newCategoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping({"/admin/categories/{catId}"})
    public void deleteCategory(@PathVariable Long catId) {
        categoriesService.deleteCategory(catId);
        log.info("Категория удалена");
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Поиск всех категорий");
        return categoriesService.getAllCategories(from, size);
    }


    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryId(@PathVariable Long catId) {

        log.info("Поиск категории по id {}", catId);

        return categoriesService.getCategoryId(catId);
    }

}
