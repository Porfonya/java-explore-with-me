package ru.practicum.categories;

import org.springframework.stereotype.Component;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriesMapper {

    public CategoryDto mapToCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
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

}
