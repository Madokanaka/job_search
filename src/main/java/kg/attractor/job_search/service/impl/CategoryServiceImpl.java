package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.exception.CategoryNotFoundException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;


    @Override
    public List<CategoryDto> findAll() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> result = categories.stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .collect(Collectors.toList());
        log.debug("Fetched {} categories", result.size());
        return result;
    }

    @Override
    public Optional<Category> findById(Integer id) {
        log.info("Fetching category with id={}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            log.warn("Category with id={} not found", id);
        } else {
            log.debug("Found category: {}", category.get());
        }
        return category;
    }

    @Override
    public Map<Integer, String> getCategories() {
        log.info("Fetching categories as map");
        Map<Integer, String> categories = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        log.debug("Fetched {} categories as map", categories.size());
        return categories;
    }

    @Override
    public boolean existsByCategoryId(Integer categoryId) {
        log.info("Fetching category with id={}", categoryId);
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public String getCategoryNameById(Integer categoryId) {
        return categoryRepository.findNameById(categoryId).orElseThrow(() -> new CategoryNotFoundException(messageSource.getMessage("error.category.not.found", new Object[]{categoryId}, LocaleContextHolder.getLocale())));
    }
}