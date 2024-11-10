package com.polar_moviechart.edgeservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polar_moviechart.edgeservice.exception.ErrorInfo;
import com.polar_moviechart.edgeservice.exception.TokenExpiredException;
import com.polar_moviechart.edgeservice.utils.CustomResponse;
import com.polar_moviechart.edgeservice.utils.PatternMatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        try {
            String token = jwtTokenProcessor.extractToken(request);

            if (requestPath.startsWith("/public") && token == null) {
                log.info("=== CombinedJwtAuthFilter public && token = null called ===");
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = validateExpiration(token);
            log.info("=== CombinedJwtAuthFilter userId = {} ===", userId);
            request.getSession().setAttribute("userId", userId);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | TokenExpiredException e) {
            e.printStackTrace();
            writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.TOKEN_EXPIRED);
        } catch (JwtException e) {
            e.printStackTrace();
            writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.TOKEN_CREATION_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.DEFAULT_ERROR);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, ErrorInfo errorInfo) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        CustomResponse<ErrorInfo> customResponse = new CustomResponse<>(errorInfo);
        String jsonResponse = new ObjectMapper().writeValueAsString(customResponse);
        response.getWriter().write(jsonResponse);
    }

    private Long validateExpiration(String token) {
        Claims claims = jwtTokenProcessor.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        jwtTokenProcessor.validateExpired(claims);
        return userId;
    }
}

