package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.BrandRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.BrandResponse;
import com.example.TechShop.service.BrandService;
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
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/admin/brands")
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody BrandRequest request
    ) {
        BrandResponse brandResponse = brandService.createBrand(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành công", brandResponse));
    }

    @GetMapping("/admin/brands")
    public ResponseEntity<ApiResponse<Page<BrandResponse>>> getAllBrand(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<BrandResponse> brandResponses = brandService.findAllBrand(
                PageRequest.of(page, size, Sort.by("createdAt").descending()),
                keyword);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", brandResponses));
    }

    @GetMapping("/admin/brands/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @PathVariable("id") Long id
    ) {
        BrandResponse brandResponse = brandService.findByBrandId(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", brandResponse));
    }

    @PutMapping("/admin/brands/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable("id") Long id,
            @Valid @RequestBody BrandRequest request
    ) {
        BrandResponse brandResponse = brandService.updateBrand(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", brandResponse));
    }

    @DeleteMapping("/admin/brands/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @PathVariable("id") Long id
    ) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

    @PostMapping("/admin/brands/{id}/status")
    public ResponseEntity<ApiResponse<BrandResponse>> toggleBrandStatus(
            @PathVariable("id") Long id
    ) {
        BrandResponse brandResponse = brandService.toggleBrandStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", brandResponse));
    }
}
