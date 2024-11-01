package com.polar_moviechart.edgeservice.filter;

import com.polar_moviechart.edgeservice.exception.TokenProcessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthFilter {

    private final JwtTokenProcessor jwtTokenProcessor;

    public JwtAuthFilter(JwtTokenProcessor jwtTokenProcessor) {
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    public void processJwtAuth(HttpServletRequest request, HttpServletResponse response) {
        if (request.getRequestURI().startsWith("/api/v1/users/login") || request.getRequestURI().startsWith("/api/edge")) {
            return;
        }

        String token = jwtTokenProcessor.extractToken(request);
        if (token == null) {
            throw new TokenProcessException("토큰이 존재하지 않습니다.");
        }

        Claims claims = jwtTokenProcessor.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        request.setAttribute("userId", userId);
    }
}
