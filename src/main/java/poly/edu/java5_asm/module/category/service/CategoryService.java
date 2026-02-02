package poly.edu.java5_asm.module.category.service;

import poly.edu.java5_asm.module.category.dto.response.CategoryResponse;

import java.util.List;

/**
 * Interface cho Category Service
 */
public interface CategoryService {

    List<CategoryResponse> getAllActiveCategories();

    List<CategoryResponse> getRootCategories();

    List<CategoryResponse> getChildCategories(Long parentId);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    long countActiveCategories();
}
