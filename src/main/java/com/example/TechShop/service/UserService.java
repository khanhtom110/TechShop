package com.example.TechShop.service;

import com.example.TechShop.dto.request.CreateUserRequest;
import com.example.TechShop.dto.request.UpdatePasswordRequest;
import com.example.TechShop.dto.request.UpdateUserRequest;
import com.example.TechShop.dto.response.UserDetailResponse;
import com.example.TechShop.dto.response.UserListResponse;
import com.example.TechShop.entity.Role;
import com.example.TechShop.entity.User;
import com.example.TechShop.exception.extended.DuplicateResourceException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.RoleRepository;
import com.example.TechShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserDetailResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username", "username", request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email", "email", request.email());
        }

        Role defaultRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .description(request.description())
                .roles(new HashSet<>(List.of(defaultRole)))
                .build();

        User savedUser = userRepository.save(user);
        return UserDetailResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponse> getAllUser(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll(pageable).map(UserListResponse::from);
        }
        return userRepository.findByNameContainingIgnoreCase(keyword, pageable).map(UserListResponse::from);
    }

    @Transactional
    public UserDetailResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email", "email", request.email());
        }

        user.setName(request.name());
        user.setPhone(request.phone());
        user.setEmail(request.email());
        user.setAddress(request.address());
        user.setDescription(request.description());

        User savedUser = userRepository.save(user);
        return UserDetailResponse.from(savedUser);
    }

    @Transactional
    public UserDetailResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(Boolean.FALSE);

        User savedUser = userRepository.save(user);
        return UserDetailResponse.from(savedUser);
    }

    @Transactional
    public UserDetailResponse updateUserPassword(Long id, UpdatePasswordRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new RuntimeException("Mật khẩu không chính xác");
        }
        if(!request.confirmPassword().equals(request.newPassword())){
            throw new RuntimeException("Xác nhận mật khẩu không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        User savedUser = userRepository.save(user);
        return UserDetailResponse.from(savedUser);
    }
}
