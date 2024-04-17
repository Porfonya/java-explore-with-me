package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.checker.Checker;
import ru.practicum.events.EventRepository;
import ru.practicum.exception.ConflictExc;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    public final CategoriesRepository categoriesRepository;
    public final EventRepository eventRepository;

    public final CategoriesMapper categoriesMapper;
    public final Checker checker;

    @Override
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        int count = categoriesRepository.countCategoriesByName(categoryDto.getName());
        if (count == 0) {
            Category category = categoriesMapper.mapToNewCategoryDto(categoryDto);
            return categoriesMapper.mapToCategory(categoriesRepository.save(category));
        } else {
            throw new ConflictExc("Категория с таким именем уже существует");
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        checker.checkerCategory(catId);
        if (eventRepository.countEventsByCategoryId(catId) == 0) {
            categoriesRepository.deleteById(catId);
        } else {
            throw new ConflictExc("Удаление категории с привязанными событиями");
        }

    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {

        Category category = checker.checkerAndReturnCategory(catId);
        if (category.getName().equals(newCategoryDto.getName())) {
            return categoriesMapper.mapToCategory(category);
        } else {
            int res = categoriesRepository.countCategoriesByName(newCategoryDto.getName());

            if (res == 0) {
                category.setName(newCategoryDto.getName());

                return categoriesMapper.mapToCategory(categoriesRepository.save(category));
            } else {
                throw new ConflictExc("Категория с таким именем уже существует");
            }
        }

    }


    @Override
    public CategoryDto getCategoryId(Long catId) {

        checker.checkerCategory(catId);
        return categoriesMapper.mapToCategory(categoriesRepository.getReferenceById(catId));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Category> categories = categoriesRepository.findAll(page).getContent();
        return categoriesMapper.mapToLisCategoriesDto(categories);
    }
}
