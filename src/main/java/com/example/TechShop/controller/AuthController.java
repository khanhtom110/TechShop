package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.LoginRequest;
import com.example.TechShop.dto.request.LogoutRequest;
import com.example.TechShop.dto.response.CommonResponse;
import com.example.TechShop.dto.response.LoginResponse;
import com.example.TechShop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.API_V1)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.authentication(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<CommonResponse> logout(@Valid @RequestBody LogoutRequest request) {
        CommonResponse response = authService.logout(request);
        return ResponseEntity.ok(response);
    }
}