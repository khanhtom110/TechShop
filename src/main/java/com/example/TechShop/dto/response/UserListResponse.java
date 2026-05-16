package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Role;
import com.example.TechShop.entity.User;

import java.util.List;
import java.util.Set;

public record UserListResponse(
        Long id,
        String avatar,
        String username,
        String phone,
        String email,
        List<String> roles,
        Boolean status
) {
    public static UserListResponse from(User user) {
        return new UserListResponse(
                user.getId(),
                user.getAvatar(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName).toList(),
                user.getStatus()
        );
    }

}