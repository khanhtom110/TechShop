package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.ProductVariantRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @PostMapping("/admin/product-variants")
    public ResponseEntity<ApiResponse<Void>> createProductVariant(
            @Valid @RequestBody ProductVariantRequest request
    ) {
        productVariantService.createProductVariant(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành công", null));

    }

    @DeleteMapping("/admin/product-variants/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductVariant(
            @PathVariable("id") Long id
    ) {
        productVariantService.deleteProductVariant(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", null)
        );
    }
}
