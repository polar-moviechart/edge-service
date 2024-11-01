package com.polar_moviechart.edgeservice.config;

import com.polar_moviechart.edgeservice.filter.JwtAuthFilter;
import com.polar_moviechart.edgeservice.filter.JwtAuthFilterOptional;
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

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthFilterOptional jwtAuthFilterOptional;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/v1/users")) {
            jwtAuthFilter.processJwtAuth(request, response);
        }
        else if (requestPath.startsWith("/api/v1/movies")) {
            jwtAuthFilterOptional.processJwtOptionalAuth(request);
        }

        filterChain.doFilter(request, response);
    }
}

