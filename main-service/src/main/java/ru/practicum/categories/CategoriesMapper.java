package ru.practicum.categories;

import org.springframework.stereotype.Component;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriesMapper {

    public Category mapToCategoriesDto(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto mapToCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public NewCategoryDto mapToNewCategoryDto(Category category) {
        return NewCategoryDto.builder()
                .name(category.getName())
                .build();
    }

    public Category mapToNewCategoryDto(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public List<CategoryDto> mapToLisCategoriesDto(Iterable<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categories) {
            categoryDtoList.add(mapToCategory(category));
        }
        return categoryDtoList;
    }

    public List<Category> mapToListCategories(Iterable<CategoryDto> categories) {
        List<Category> categoryList = new ArrayList<>();
        for (CategoryDto category : categories) {
            categoryList.add(mapToCategoriesDto(category));
        }
        return categoryList;
    }
}
