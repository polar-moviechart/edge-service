package com.polar_moviechart.edgeservice.filter;

import com.polar_moviechart.edgeservice.exception.TokenProcessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthFilter implements Filter {

    private final JwtTokenProcessor jwtTokenProcessor;

    public JwtAuthFilter(JwtTokenProcessor jwtTokenProcessor) {
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = jwtTokenProcessor.extractToken(httpRequest);
        if (token == null) {
            throw new TokenProcessException("토큰이 존재하지 않습니다.");
        }

        Claims claims = jwtTokenProcessor.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        request.setAttribute("userId", userId);

        chain.doFilter(request, response);
    }
}
