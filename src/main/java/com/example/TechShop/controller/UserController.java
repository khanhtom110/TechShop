package com.example.TechShop.controller;

import com.example.TechShop.constant.ApiPath;
import com.example.TechShop.dto.request.CreateUserRequest;
import com.example.TechShop.dto.request.UpdatePasswordRequest;
import com.example.TechShop.dto.request.UpdateUserRequest;
import com.example.TechShop.dto.response.ApiResponse;
import com.example.TechShop.dto.response.UserDetailResponse;
import com.example.TechShop.dto.response.UserListResponse;
import com.example.TechShop.security.SecurityUtils;
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

    //Method for User

    @GetMapping("/users/profile")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserDetailResponse userDetailResponse = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", userDetailResponse));
    }

    @PutMapping("/users/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateUserPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Thành công", null));
    }

    @PutMapping("/users/profile")
    public ResponseEntity<ApiResponse<UserDetailResponse>> updateMyProfile(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserDetailResponse userDetailResponse = userService.updateUser(userId, request);
        return ResponseEntity
                .ok(ApiResponse.ok("Update thành công", userDetailResponse));
    }

    //Method for Admin

    @PostMapping("/admin/users")
    public ResponseEntity<ApiResponse<UserDetailResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo thành công", userService.createUser(request)));
    }

    @GetMapping("/admin/users")
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

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .ok(ApiResponse.ok("Thành công", userService.getUserById(id)));
    }

    @PutMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity
                .ok(ApiResponse.ok("Update thành công", userService.updateUser(id, request)));
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable("id") Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity
                .ok(ApiResponse.ok("Xóa thành công", null));
    }

    //Dùng để admin khóa hoặc mở khóa một user trong hệ thống
    @PostMapping("/admin/users/{id}/status")
    public ResponseEntity<ApiResponse<UserListResponse>> toggleUserStatus(
            @PathVariable("id") Long id
    ){
        UserListResponse userListResponse=userService.toggleUserStatus(id);

        return ResponseEntity.ok(ApiResponse.ok("Thành công",userListResponse));
    }
}
