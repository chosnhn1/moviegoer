package com.moviegoer.backend.util;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil implements Serializable {
    private static final long serialVersionUID = 1L;

    // JWT 토큰 관련 사항은 구성에서 별도 관리
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private Long JWT_VALIDITY;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Long getUserIdFromToken(String token) {
        String jwt;
        if (token.startsWith("Bearer ")) {
            jwt = token.substring(7);
        } else {
            jwt = token;
        }
        
        return getClaimFromToken(jwt, claims -> claims.get("userId", Long.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts
            .parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 토큰 생성
    public String generateToken(UserDetails userDetails, Long userId) {

        // Claims는 반드시 필요한 내용으로 최소화
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    // 토큰 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);

        return (
            // 요청-토큰 일치 여부 체크
            username.equals(userDetails.getUsername())
            // 토큰 만료 여부 체크
            && !isTokenExpired(token)
        );
    }



}
