package com.example.TechShop.dto.response;

import com.example.TechShop.entity.CartItem;

import java.math.BigDecimal;

public record CartItemResponse(
        String productImageUrl,
        String productName,
        BigDecimal price,
        Integer quantity,
        BigDecimal subTotal
) {
    public static CartItemResponse from(CartItem item){
        return new CartItemResponse(
                item.getProductVariant().getProduct().getPrimaryImageUrl(),
                item.getProductVariant().getProduct().getName(),
                item.getProductVariant().getPrice(),
                item.getQuantity(),
                item.getSubTotal()
        );
    }
}
