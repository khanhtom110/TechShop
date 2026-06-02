package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.CartItemRequest;
import com.example.TechShop.dto.request.UpdateCartItemRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.CartResponse;
import com.example.TechShop.security.SecurityUtils;
import com.example.TechShop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse<Void>> addToCart(
            @Valid @RequestBody CartItemRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.addToCart(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành công", null));
    }

    @GetMapping("/cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCartByUserId() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.ok("Thành công", cartService.getCartByUserId(userId)));
    }

    @PostMapping("/cart/update")
    public ResponseEntity<ApiResponse<Void>> updateCartItemQuantity(
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.updateCartItemQuantity(userId, request.productVariantId(), request.newQuantity());
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

    @PostMapping("/cart/remove/{variantId}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(
            @PathVariable Long variantId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.removeCartItem(userId, variantId);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

    @DeleteMapping("/cart")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

}
