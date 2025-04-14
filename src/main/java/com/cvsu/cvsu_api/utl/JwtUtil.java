package com.cvsu.cvsu_api.utl;

import com.cvsu.cvsu_api.model.UserProfileModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 7 Days

    // ✅ Constructor to initialize SECRET_KEY
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Generate JWT Token
    public String generateToken(UserProfileModel user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("fullName", user.getFullName())
                .claim("roleName", user.getRoleName())
                .claim("id", user.getId())
                .claim("position", user.getPosition())
                .claim("username", user.getUsername())
                .claim("password", user.getPassword())
                .claim("employeeNo", user.getEmployeeNo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Validate Token
    public boolean validateToken(String token) {
        try {
            System.out.println("Token: " + token);
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (JwtException e) {
            System.out.println("Invalid token");
        }
        return false;
    }

    // ✅ Extract User Claims
    public Claims extractUserClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    // ✅ Extract Username from Token
    public String extractUsername(String token) {
        Claims claims = extractUserClaims(token);
        return (claims != null) ? claims.getSubject() : null;
    }
}
