package com.polar_moviechart.edgeservice.config;

import com.polar_moviechart.edgeservice.exception.ErrorInfo;
import com.polar_moviechart.edgeservice.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProcessor {

    private final String secretKey;

    public JwtTokenProcessor(
            @Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = secretKey;
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
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
