package ru.practicum.categories;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto addCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    CategoryDto getCategoryId(Long id);

    List<CategoryDto> getAllCategories(int from, int size);
}
