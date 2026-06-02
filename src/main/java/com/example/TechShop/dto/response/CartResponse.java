package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        List<CartItemResponse> cartItemResponses,
        BigDecimal subTotal,
        BigDecimal shipping,
        BigDecimal discount,
        BigDecimal total
) {
    public static CartResponse from(Cart cart) {
        List<CartItemResponse> cartItemResponses = cart.getCartItems().stream()
                .map(CartItemResponse::from).toList();

        BigDecimal subtotal = cartItemResponses.stream()
                .map(CartItemResponse::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shipping = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;

        BigDecimal total = subtotal.add(shipping).subtract(discount);

        return new CartResponse(
                cartItemResponses,
                subtotal,
                shipping,
                discount,
                total
        );
    }
}
