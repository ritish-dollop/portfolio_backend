package com.portfolio.security;

import com.portfolio.constant.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationInMs;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private long jwtRefreshExpirationInMs;

    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateToken(Authentication authentication) {
        return generateAccessToken(authentication);
    }

    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateAccessToken(userPrincipal);
    }

    public String generateAccessToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return generateAccessTokenFromUsername(userDetails.getUsername(), roles);
    }

    public String generateAccessTokenFromUsername(String username, String roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim(AppConstants.CLAIM_TOKEN_TYPE, AppConstants.TOKEN_TYPE_ACCESS)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateRefreshToken(userPrincipal);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshTokenFromUsername(userDetails.getUsername());
    }

    public String generateRefreshTokenFromUsername(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationInMs);

        return Jwts.builder()
                .subject(username)
                .claim(AppConstants.CLAIM_TOKEN_TYPE, AppConstants.TOKEN_TYPE_REFRESH)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = getClaimsFromJwt(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getTokenType(String token) {
        Claims claims = getClaimsFromJwt(token);
        return claims != null ? claims.get(AppConstants.CLAIM_TOKEN_TYPE, String.class) : null;
    }

    public boolean isAccessToken(String token) {
        String tokenType = getTokenType(token);
        return tokenType == null || AppConstants.TOKEN_TYPE_ACCESS.equalsIgnoreCase(tokenType);
    }

    public boolean isRefreshToken(String token) {
        String tokenType = getTokenType(token);
        return AppConstants.TOKEN_TYPE_REFRESH.equalsIgnoreCase(tokenType);
    }

    public long getExpirationInMs() {
        return jwtExpirationInMs;
    }

    public long getRefreshExpirationInMs() {
        return jwtRefreshExpirationInMs;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature/token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token) && isAccessToken(token);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token) && isRefreshToken(token);
    }

    private Claims getClaimsFromJwt(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Could not parse JWT claims: {}", e.getMessage());
            return null;
        }
    }
}
