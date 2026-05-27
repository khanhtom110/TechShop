package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandRequest(
        @NotBlank(message = "Tên Brand không được để trống")
        @Size(min = 2, max = 255, message = "Tên Brand phải từ 2-255 ký tự")
        String name,

        @Size(max = 255, message = "Logo không quá 255 ký tự")
        String logoUrl,

        @Size(max = 1000, message = "Mô tả không quá 1000 ký tự")
        String description
) {
}
