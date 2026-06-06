package com.example.TechShop.dto.response;

import com.example.TechShop.entity.OrderDetail;

import java.math.BigDecimal;

public record OrderDetailResponse(
        Long productVariantId,
        String productName,
        String productUrlImage,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
    public static OrderDetailResponse from(OrderDetail orderDetail) {
        return new OrderDetailResponse(
                orderDetail.getProductVariant().getId(),
                orderDetail.getProductVariant().getProduct().getName(),
                orderDetail.getProductVariant().getProduct().getPrimaryImageUrl(),
                orderDetail.getQuantity(),
                orderDetail.getPrice(),
                orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()))
        );
    }
}
