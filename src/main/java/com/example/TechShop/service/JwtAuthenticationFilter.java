package com.example.TechShop.service;

import com.example.TechShop.repository.InvalidatedTokenRepository;
import com.example.TechShop.security.JwtProvider;
import com.example.TechShop.security.UserDetailsServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        //Khách vãng lai
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Không tìm thấy Bearer Token: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            boolean invalid = invalidatedTokenRepository.existsById(jwtId);
            if (invalid) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token chưa được xác thực");
                return;
            }
        } catch (ParseException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Token không thể giải mã");
            return;
        }

        String username = jwtProvider.extractUsername(token);
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isValid = jwtProvider.isTokenValid(token, userDetails);
            if (isValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } else {
            log.warn("Invalid token for user: {}", username);
        }

        filterChain.doFilter(request, response);
    }

    public void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        String jsonResponse = String.format(
                "{\"status\": %d, \"message\": \"%s\", \"data\": null}",
                status, message
        );

        response.getWriter().write(jsonResponse);
    }
}
