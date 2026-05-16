package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Role;
import com.example.TechShop.entity.User;

import java.util.List;

public record UserDetailResponse(
        Long id,
        String username,
        String name,
        String phone,
        String email,
        String address,
        String avatar,
        Boolean status,
        String description,
        List<String> roles
) {
    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getAvatar(),
                user.getStatus(),
                user.getDescription(),
                user.getRoles().stream()
                        .map(Role::getName).toList()
        );
    }
}
