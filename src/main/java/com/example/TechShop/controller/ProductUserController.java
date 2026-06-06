package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.ProductResponse;
import com.example.TechShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
public class ProductUserController {

    private final ProductService productService;

    // API Công khai lấy danh sách sản phẩm cho trang chủ React
    // Đường dẫn thực tế: /api/v1/products
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProductForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<ProductResponse> productResponses = productService.findAllProduct(
                PageRequest.of(page, size, Sort.by("createdAt").descending()),
                keyword
        );

        return ResponseEntity.ok(
                ApiResponse.ok("Lấy danh sách sản phẩm trang chủ thành công", productResponses));
    }
}