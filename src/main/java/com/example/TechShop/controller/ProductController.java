package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.ProductRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.ProductResponse;
import com.example.TechShop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
public class ProductController {
    private final ProductService productService;

    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse productResponse = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành công", productResponse));
    }

    @GetMapping("/admin/products")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<ProductResponse> productResponses = productService.findAllProduct(
                PageRequest.of(page, size, Sort.by("createdAt").descending()),
                keyword
        );

        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", productResponses));
    }

    @GetMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable("id") Long id
    ) {
        ProductResponse productResponse = productService.findProductById(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", productResponse));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse productResponse = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", productResponse));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", null));
    }

    @PostMapping("/admin/products/{id}/status")
    public ResponseEntity<ApiResponse<ProductResponse>> toggleProductStatus(
            @PathVariable("id") Long id
    ) {
        ProductResponse productResponse = productService.toggleProductStatus(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Thành công", productResponse));
    }
}
