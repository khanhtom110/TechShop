package com.example.TechShop.dto.response;

import org.springframework.http.HttpStatus;

public record CommonResponse(
        HttpStatus status,

        String message
) {
}
