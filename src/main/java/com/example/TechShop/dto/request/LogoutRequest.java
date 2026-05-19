package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank(message = "Token không tồn tại")
        String token
) {
}
