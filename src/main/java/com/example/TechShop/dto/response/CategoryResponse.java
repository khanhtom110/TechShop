package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Category;

public record CategoryResponse(
        Long id,
        Long parentCategoryId,
        String name,
        Boolean status,
        String description
) {
    public static CategoryResponse from(Category category) {
        if (category.getParentCategory() == null) {
            return new CategoryResponse(
                    category.getId(),
                    null,
                    category.getName(),
                    category.getStatus(),
                    category.getDescription());
        }
        return new CategoryResponse(
                category.getId(),
                category.getParentCategory().getId(),
                category.getName(),
                category.getStatus(),
                category.getDescription()
        );
    }
}
