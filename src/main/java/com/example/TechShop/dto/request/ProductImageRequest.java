package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductImageRequest(
        @NotNull(message = "Mã sản phẩm không được để trống")
        @Positive(message = "Mã sản phẩm phải lớn hơn 0")
        Long productId,

        @NotBlank(message = "Ảnh không thể để trống")
        String imageUrl,

        @NotNull(message = "Số thứ tự không được để trống")
        @PositiveOrZero(message = "Số thứ tự của ảnh phải lớn hoặc bằng 0")
        Integer displayIndex,

        @NotNull(message = "Cờ trạng thái ảnh đại diện không được để trống")
        Boolean isPrimary
) {
}
