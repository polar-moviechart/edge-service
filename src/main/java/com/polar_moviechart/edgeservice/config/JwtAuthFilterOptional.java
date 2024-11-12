package com.polar_moviechart.edgeservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polar_moviechart.edgeservice.exception.ErrorInfo;
import com.polar_moviechart.edgeservice.exception.TokenExpiredException;
import com.polar_moviechart.edgeservice.utils.CustomResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthFilterOptional extends AbstractGatewayFilterFactory<JwtAuthFilterOptional.Config> {

    private final JwtTokenProcessor jwtTokenProcessor;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthFilterOptional(JwtTokenProcessor jwtTokenProcessor) {
        super(Config.class);
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                String token = jwtTokenProcessor.extractToken(exchange.getRequest());

                if (token == null) {
                    return chain.filter(exchange);
                }

                Claims claims = jwtTokenProcessor.getClaims(token);
                jwtTokenProcessor.validateExpired(claims);

                // 기존 헤더를 복사하고 새로운 헤더를 추가
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
                headers.add("X-User-Id", claims.getSubject());
                // 새로운 ServerHttpRequestDecorator를 생성하여 헤더를 교체
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public HttpHeaders getHeaders() {
                        return headers;
                    }
                };

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (ExpiredJwtException | TokenExpiredException e) {
                log.error(e.getMessage(), e);
                return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.TOKEN_EXPIRED);
            } catch (JwtException e) {
                log.error(e.getMessage(), e);
                return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.TOKEN_CREATION_ERROR);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ErrorInfo.DEFAULT_ERROR);
            }
        };
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, ErrorInfo errorInfo) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        CustomResponse<ErrorInfo> customResponse = new CustomResponse<>(errorInfo);

        try {
            String jsonResponse = objectMapper.writeValueAsString(customResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    public static class Config {
    }
}
