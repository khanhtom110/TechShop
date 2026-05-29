package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductResponse(
        Long id,
        Long categoryId,
        Long brandId,
        String name,
        List<ProductImageResponse> productImages,
        BigDecimal basePrice,
        BigDecimal salePrice,
        Map<String, Object> specifications,
        List<ProductVariantResponse> productVariants,
        Boolean status,
        String description
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getBrand().getId(),
                product.getName(),
                product.getProductImages()
                        .stream().map(ProductImageResponse::from)
                        .toList(),
                product.getBasePrice(),
                product.getSalePrice(),
                product.getSpecifications(),
                product.getProductVariants()
                        .stream()
                        .filter(variant -> Boolean.TRUE.equals(variant.getStatus()))
                        .map(ProductVariantResponse::from)
                        .toList(),
                product.getStatus(),
                product.getDescription()
        );
    }
}
