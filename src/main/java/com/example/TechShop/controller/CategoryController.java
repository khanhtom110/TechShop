package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.CategoryRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.CategoryResponse;
import com.example.TechShop.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo thành công", categoryResponse));
    }

    @GetMapping("/admin/categories")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<CategoryResponse> categoryResponses = categoryService.findAllCategory(
                PageRequest.of(page, size, Sort.by("createdAt").descending()),
                keyword
        );
        return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryResponses));
    }

    @GetMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id
    ) {
        CategoryResponse categoryResponse = categoryService.findCategoryById(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryResponse));
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryResponse));
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

    @PostMapping("/admin/categories/{id}/status")
    public ResponseEntity<ApiResponse<CategoryResponse>> toggleCategoryStatus(
            @PathVariable Long id
    ){
        CategoryResponse categoryResponse = categoryService.toggleCategoryStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryResponse));
    }
}
