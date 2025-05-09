package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {


    List<CategoryDto> findAll();
    Optional<Category> findById(Integer id);
    Map<Integer, String> getCategories();

    boolean existsByCategoryId(Integer categoryId);

    String getCategoryNameById(Integer categoryId);
}