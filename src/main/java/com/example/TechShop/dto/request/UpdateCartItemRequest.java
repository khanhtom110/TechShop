package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateCartItemRequest(
        @NotNull(message = "VariantId không được để trống")
        @Positive(message = "VariantId phải lớn hơn 0")
        Long productVariantId,

        @PositiveOrZero(message = "Số lượng mới phải lớn hơn hoặc bằng 0")
        Integer newQuantity
) {
}
