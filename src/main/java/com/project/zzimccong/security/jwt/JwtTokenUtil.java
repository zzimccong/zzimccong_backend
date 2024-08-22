package com.project.zzimccong.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    // 액세스 토큰 생성 메서드
    public String generateToken(String userId, String userType) {
        return generateToken(userId, userType, expiration);
    }

    // 리프레시 토큰 생성 메서드
    public String generateRefreshToken(String userId) {
        return generateToken(userId, null, refreshExpiration);
    }

    // 공통적인 토큰 생성 로직
    private String generateToken(String userId, String userType, Long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        if (userType != null) {
            claims.put("userType", userType);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getUserTypeFromToken(String token) {
        return (String) getClaimsFromToken(token).get("userType");
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰이 만료되었는지 확인하는 메서드
    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
