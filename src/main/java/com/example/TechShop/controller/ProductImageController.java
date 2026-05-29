package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.ProductImageRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping("/admin/product-images")
    public ResponseEntity<ApiResponse<Void>> createProductImage(
            @Valid @RequestBody ProductImageRequest request
    ) {
        productImageService.createProductImage(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành công", null));
    }

    @DeleteMapping("/admin/product-images/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @PathVariable("id") Long id
    ) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", null)
        );
    }
}
