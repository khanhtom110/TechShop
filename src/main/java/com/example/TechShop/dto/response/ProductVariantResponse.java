package com.example.TechShop.dto.response;

import com.example.TechShop.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariantResponse(
        Long id,
        Map<String, String> attributes,
        BigDecimal price,
        Integer stockQuantity
) {
    public static ProductVariantResponse from(ProductVariant variant){
        return new ProductVariantResponse(
                variant.getId(),
                variant.getAttributes(),
                variant.getPrice(),
                variant.getStockQuantity()
        );
    }
}
