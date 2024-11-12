package com.polar_moviechart.edgeservice.domain.kakao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    @Value("${kakao.client.id}")
    private String clientId;

    private final String redirectUri = "http://localhost:8080/public/api/edge/users/kakao/login/callback";
    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
    private final WebClient webClient;

    public Mono<KaKaoTokenResponse> getTokenAndRedirectUser(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(KaKaoTokenResponse.class);
    }

    public Mono<KakaoUserInfoDto> getUserId(String kakaoAccessToken) {
        return webClient.post()
                .uri(userInfoUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBearerAuth(kakaoAccessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfoDto.class);
    }
}
