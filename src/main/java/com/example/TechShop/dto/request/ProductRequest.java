package com.example.TechShop.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Map;

public record ProductRequest(
        @NotNull(message = "Mã danh mục không được để trống")
        @Positive(message = "Mã danh mục phải lớn hơn 0")
        Long categoryId,

        @NotNull(message = "Mã thương hiệu không được để trống")
        @Positive(message = "Mã thương hiệu phải lớn hơn 0")
        Long brandId,

        @NotBlank(message = "Tên sản phẩm không được để trống")
        @Size(min = 2,max = 255,message = "Tên sản phẩm phải từ 2-255 ký tự")
        String name,

        @NotNull(message = "Giá gốc không được để trống")
        @PositiveOrZero(message = "Giá gốc phải lớn hơn hoặc bằng 0")
        BigDecimal basePrice,

        @PositiveOrZero(message = "Giá sale phải lớn hơn hoặc bằng 0")
        BigDecimal salePrice,

        @NotEmpty(message = "Thông số kỹ thuật không được để trống")
        Map<String, Object> specifications,

        @Size(max = 1000, message = "Mô tả không quá 1000 ký tự")
        String description
) {
}
