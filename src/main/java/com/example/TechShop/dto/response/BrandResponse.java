package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Brand;

public record BrandResponse(
        Long id,
        String name,
        String logoUrl,
        Boolean status,
        String description
){
    public static BrandResponse from(Brand brand){
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getLogoUrl(),
                brand.getStatus(),
                brand.getDescription()
        );
    }
}
