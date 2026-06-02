package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequest(
        @NotNull(message = "Mã sản phẩm không được để trống")
        @Positive(message = "Mã sản phẩm phải lớn hơn 0")
        Long productVariantId,

        @Positive(message = "Số lượng phải lớn hơn 0")
        Integer quantity
) {
}
