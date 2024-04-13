package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.checker.Checker;
import ru.practicum.exception.EventNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    public final CategoriesRepository categoriesRepository;

    public final CategoriesMapper categoriesMapper;
    public final Checker checker;

    @Override
    public CategoryDto addCategory(NewCategoryDto categoryDto) {

        Category category = categoriesMapper.mapToNewCategoryDto(categoryDto);
        return categoriesMapper.mapToCategory(categoriesRepository.save(category));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (checker.checkerAndReturnCategory(catId) != null) {
            categoriesRepository.deleteById(catId);
        } else {
            throw new EventNotFoundException(catId);
        }

    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category existCat = categoriesRepository.findById(catId)
                .orElseThrow(() -> new EventNotFoundException(catId));
        Category updateCat = categoriesMapper.mapToNewCategoryDto(newCategoryDto);
        updateCat.setId(existCat.getId());
        //   updateCat.setName(existCat.getName());
        return categoriesMapper.mapToCategory(categoriesRepository.save(updateCat));
    }


    @Override
    public CategoryDto getCategoryId(Long catId) {

        checker.checkerCategory(catId);
        return categoriesMapper.mapToCategory(categoriesRepository.getReferenceById(catId));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {

        List<Category> categories = categoriesRepository.findAll();
        return categoriesMapper.mapToLisCategoriesDto(categories);
    }
}
