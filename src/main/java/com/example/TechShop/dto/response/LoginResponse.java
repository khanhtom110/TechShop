package com.example.TechShop.dto.response;

public record LoginResponse(
        String accessToken,

        String refreshToken,

        boolean authenticated
) {
}
