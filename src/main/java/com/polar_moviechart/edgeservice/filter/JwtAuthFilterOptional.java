package com.polar_moviechart.edgeservice.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthFilterOptional implements Filter {

    private final JwtTokenProcessor jwtTokenProcessor;

    public JwtAuthFilterOptional(JwtTokenProcessor jwtTokenProcessor) {
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = jwtTokenProcessor.extractToken(httpRequest);

        if (token != null) {
            Claims claims = jwtTokenProcessor.getClaims(token);
            Long userId = Long.valueOf(claims.getSubject());
            request.setAttribute("userId", userId);
        }

        chain.doFilter(request, response);
    }
}
