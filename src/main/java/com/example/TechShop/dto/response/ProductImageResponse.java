package com.example.TechShop.dto.response;

import com.example.TechShop.entity.ProductImage;

public record ProductImageResponse(
        Long id,
        String imageUrl,
        Boolean isPrimary,
        Integer displayIndex
) {
    public static ProductImageResponse from(ProductImage image){
        return new ProductImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getIsPrimary(),
                image.getDisplayIndex()
        );
    }
}
