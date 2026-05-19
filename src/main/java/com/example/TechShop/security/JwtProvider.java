package com.example.TechShop.security;

import com.example.TechShop.entity.User;
import com.example.TechShop.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateToken(User user, long expirationTime) {
        try {
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

            List<String> stringRoles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName())
                    .toList();

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("authorities", stringRoles)
                    .claim("userId", user.getId())
                    .claim("email", user.getEmail())
                    .build();

            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

            signedJWT.sign(new MACSigner(secretKey.getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        final JWTClaimsSet claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private JWTClaimsSet extractAllClaim(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
            boolean isVerified = signedJWT.verify(jwsVerifier);
            if (!isVerified) {
                throw new RuntimeException("Chữ ký không hợp lệ");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("Token không hợp lệ", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    public String extractTokenId(String token) {
        return extractClaim(token, JWTClaimsSet::getJWTID);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, JWTClaimsSet::getExpirationTime);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String jwtId = extractTokenId(token);
        boolean isInvalidated = invalidatedTokenRepository.existsById(jwtId);
        return username.equals(userDetails.getUsername()) && !isInvalidated && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
