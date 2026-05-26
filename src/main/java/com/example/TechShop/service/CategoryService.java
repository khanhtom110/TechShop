package com.example.TechShop.service;

import com.example.TechShop.dto.request.CategoryRequest;
import com.example.TechShop.dto.response.CategoryResponse;
import com.example.TechShop.entity.Category;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.DuplicateResourceException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        Category parentCategory = null;
        if (request.parentCategoryId() != null && request.parentCategoryId() > 0) {
            if (!categoryRepository.existsById(request.parentCategoryId())) {
                throw new ResourceNotFoundException("Category", "parentId", request.parentCategoryId());
            }
            parentCategory = categoryRepository.getReferenceById(request.parentCategoryId());
        }

        Category category = Category.builder()
                .parentCategory(parentCategory)
                .name(request.name())
                .description(request.description())
                .build();
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAllCategory(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return categoryRepository.findAll(pageable).map(CategoryResponse::from);
        }
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable).map(CategoryResponse::from);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(Long id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryResponse.from(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        //Check ten trung
        if (!category.getName().equals(request.name()) && categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        //Check categoryId trung parentCategoryId
        if(Objects.equals(category.getId(), request.parentCategoryId())){
            throw new AppException(400,"Danh mục không hợp lệ");
        }

        //Check parent id
        Category parentCategory = null;
        if (request.parentCategoryId() != null && request.parentCategoryId() > 0) {
            //Kiem tra ton tai
            if (!categoryRepository.existsById(request.parentCategoryId())) {
                throw new ResourceNotFoundException("Category", "parentId", request.parentCategoryId());
            }
            parentCategory = categoryRepository.getReferenceById(request.parentCategoryId());
        }

        category.setParentCategory(parentCategory);
        category.setName(request.name());
        category.setDescription(request.description());

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setStatus(false);
        return CategoryResponse.from(category);
    }

    @Transactional
    public CategoryResponse toggleCategoryStatus(Long id){
        Category category=categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setStatus(!category.getStatus());
        return CategoryResponse.from(category);
    }
}
