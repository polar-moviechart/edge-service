package com.polar_moviechart.edgeservice.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthFilterOptional {

    private final JwtTokenProcessor jwtTokenProcessor;

    public JwtAuthFilterOptional(JwtTokenProcessor jwtTokenProcessor) {
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    public void processJwtOptionalAuth(HttpServletRequest request) {
        String token = jwtTokenProcessor.extractToken(request);

        if (token != null) {
            Claims claims = jwtTokenProcessor.getClaims(token);
            Long userId = Long.valueOf(claims.getSubject());
            request.setAttribute("userId", userId);
        }
    }
}
