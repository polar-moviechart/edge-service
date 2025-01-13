package com.polar_moviechart.edgeservice.domain.kakao.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoTokenService {

    @Value("${kakao.client.id}")
    private String clientId;
    @Value("${edge.service.url}")
    private String edgeServiceUrl;

    private String redirectUri;
    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
    private final WebClient webClient;

    @PostConstruct
    public void init() {
        this.redirectUri = String.format("%s/public/api/edge/users/kakao/login/callback", edgeServiceUrl);
    }

    public Mono<KaKaoTokenResponse> getTokenAndRedirectUser(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        log.info("redirect Url = {}", redirectUri);

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
