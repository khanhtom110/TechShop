package com.example.TechShop.service;

import com.example.TechShop.dto.request.LoginRequest;
import com.example.TechShop.dto.request.LogoutRequest;
import com.example.TechShop.dto.response.CommonResponse;
import com.example.TechShop.dto.response.LoginResponse;
import com.example.TechShop.entity.InvalidatedToken;
import com.example.TechShop.entity.User;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.repository.InvalidatedTokenRepository;
import com.example.TechShop.repository.UserRepository;
import com.example.TechShop.security.JwtProvider;
import com.example.TechShop.security.UserDetailsServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Value("${jwt.access.expiration_time}")
    long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refresh.expiration_time}")
    long REFRESH_EXPIRATION_TIME;

    public LoginResponse authentication(LoginRequest request) {
        //usernamme
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new AppException(
                        401, "Username hoặc password chưa đúng"));
        //password
        boolean auth = passwordEncoder.matches(request.password(), user.getPassword());
        if (!auth) {
            throw new AppException(401, "Username hoặc password chưa đúng");
        }

        String accessToken = jwtProvider.generateToken(user, ACCESS_EXPIRATION_TIME);
        String refreshToken = jwtProvider.generateToken(user, REFRESH_EXPIRATION_TIME);

        return new LoginResponse(accessToken, refreshToken, true);
    }

    public CommonResponse logout(LogoutRequest request) {
        String username = jwtProvider.extractUsername(request.token());
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (!jwtProvider.isTokenValid(request.token(), userDetails)) {
            throw new AppException(401, "Token không hợp lệ");
        }

        String jwtId = jwtProvider.extractTokenId(request.token());
        Date expirationTime = jwtProvider.extractExpiration(request.token());

        if (invalidatedTokenRepository.existsById(jwtId)) {
            throw new AppException(400, "Token đã hết hiệu lực");
        }

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryDate(expirationTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        return new CommonResponse(
                HttpStatus.OK, "Logout thành công"
        );
    }
}
