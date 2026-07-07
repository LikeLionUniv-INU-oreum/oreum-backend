package com.likelion.orum.global.security.jwt;

import com.likelion.orum.domain.auth.exception.AuthErrorCode;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // AccessToken 발급
    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("universityEmail", user.getUniversityEmail())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    // 인증 사용자 정보를 추출
    public AuthenticatedUser getAuthenticatedUser(String token) {
        Claims claims = parseClaims(token);

        String subject = claims.getSubject();
        String universityEmail = claims.get("universityEmail", String.class);

        if (subject == null || universityEmail == null) {
            throw new GeneralException(SecurityErrorCode.INVALID_TOKEN);
        }

        try {
            return new AuthenticatedUser(
                    Long.valueOf(subject),
                    universityEmail
            );
        } catch (NumberFormatException e) {
            throw new GeneralException(SecurityErrorCode.INVALID_TOKEN);
        }
    }

    // JWT 파싱 및 유효성 검증
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new GeneralException(SecurityErrorCode.EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new GeneralException(SecurityErrorCode.INVALID_TOKEN);
        }
    }
}
