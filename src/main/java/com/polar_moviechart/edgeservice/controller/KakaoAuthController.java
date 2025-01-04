package com.polar_moviechart.edgeservice.controller;

import com.polar_moviechart.edgeservice.domain.kakao.service.KakaoTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/edge/users/kakao")
public class KakaoAuthController {
    private final KakaoTokenService kakaoTokenService;

    @GetMapping("/login/callback")
    public Mono<Void> getKakaoExternalId(@RequestParam(name = "code") String code, ServerWebExchange exchange) {
        return kakaoTokenService.getTokenAndRedirectUser(code)
                .flatMap(kaKaoTokenResponse ->
                        kakaoTokenService.getUserId(kaKaoTokenResponse.getAccess_token())
                .flatMap(kakaoUserInfoDto -> {
                    String redirectUrl = "http://localhost:3000/kakaoAuth?id=" + kakaoUserInfoDto.getId();
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create(redirectUrl));
                    return response.setComplete();
                }));
    }
}
