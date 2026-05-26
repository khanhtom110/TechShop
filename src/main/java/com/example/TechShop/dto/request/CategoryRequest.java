package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        Long parentCategoryId,

        @NotBlank(message = "Tên danh mục không được để trống")
        @Size(min = 2,max = 255, message = "Tên danh mục từ 2-255 ký tự")
        String name,

        @Size(max = 1000,message = "Mô tả không quá 1000 ký tự")
        String description
) {
}
