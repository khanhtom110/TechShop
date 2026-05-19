package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.CreateUserRequest;
import com.example.TechShop.dto.request.UpdatePasswordRequest;
import com.example.TechShop.dto.request.UpdateUserRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.UserDetailResponse;
import com.example.TechShop.dto.response.UserListResponse;
import com.example.TechShop.service.UserService;
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
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserDetailResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo thành công", userService.createUser(request)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserListResponse>>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Page<UserListResponse> users = userService.getAllUser(
                PageRequest.of(page, size, Sort.by("createdAt").descending()),
                search
        );

        return ResponseEntity.ok(ApiResponse.ok("Thành công", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .ok(ApiResponse.ok("Thành công", userService.getUserById(id)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity
                .ok(ApiResponse.ok("Update thành công", userService.updateUser(id, request)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable("id") Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity
                .ok(ApiResponse.ok("Xóa thành công", null));
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUserPassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        return ResponseEntity
                .ok(ApiResponse.ok("Update thành công",userService.updateUserPassword(id, request)));
    }
}
