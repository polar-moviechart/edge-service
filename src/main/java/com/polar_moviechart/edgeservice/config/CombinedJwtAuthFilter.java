package com.polar_moviechart.edgeservice.config;

import com.polar_moviechart.edgeservice.utils.PatternMatcher;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CombinedJwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProcessor jwtTokenProcessor;
    private final PatternMatcher patternMatcher = new PatternMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestPath = request.getRequestURI();
        String token = jwtTokenProcessor.extractToken(request);

        if (patternMatcher.isPublicUrl(requestPath) && token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = validateExpiration(token);
        request.setAttribute("userId", userId);

        filterChain.doFilter(request, response);
    }

    private Long validateExpiration(String token) {
        Claims claims = jwtTokenProcessor.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        jwtTokenProcessor.validateExpired(claims);
        return userId;
    }
}

