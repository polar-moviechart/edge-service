package com.polar_moviechart.edgeservice.config;

import ch.qos.logback.core.spi.ErrorCodes;
import com.polar_moviechart.edgeservice.exception.ErrorInfo;
import com.polar_moviechart.edgeservice.exception.TokenExpiredException;
import com.polar_moviechart.edgeservice.exception.TokenProcessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProcessor {

    private final String secretKey;

    public JwtTokenProcessor(
            @Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = secretKey;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (RuntimeException e) {
            throw new JwtException("올바르지 않은 토큰입니다.", e);
        }
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public void validateExpired(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new TokenExpiredException(ErrorInfo.TOKEN_EXPIRED);
        }
    }
}
