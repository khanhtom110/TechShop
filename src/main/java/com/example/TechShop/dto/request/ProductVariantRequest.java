package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariantRequest(
        @NotNull(message = "Mã sản phẩm không được để trống")
        @Positive(message = "Mã sản phẩm phải lớn hơn 0")
        Long productId,

        @NotEmpty(message = "Thông số biến thể không được để trống")
        Map<String, String> attributes,

        @NotNull(message = "Giá không được để trống")
        @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
        BigDecimal price,

        @NotNull(message = "Số lượng hàng tồn kho không được để trống")
        @PositiveOrZero(message = "Số lượng hàng tồn kho phải lớn hơn hoặc bằng 0")
        Integer stockQuantity
) {
}
