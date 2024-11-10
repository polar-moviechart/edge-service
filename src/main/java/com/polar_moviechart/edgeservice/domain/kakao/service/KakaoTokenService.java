package com.polar_moviechart.edgeservice.domain.kakao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {
    private final RestTemplate restTemplate;

    @Value("${kakao.client.id}")
    private String clientId;

    private final String redirectUri = "http://localhost:8080/public/api/edge/users/kakao/login/callback";
    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

    public KaKaoTokenResponse getTokenAndRedirectUser(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = String.format("grant_type=authorization_code" +
                        "&client_id=%s" +
                        "&redirect_uri=%s" +
                        "&code=%s",
                clientId,
                redirectUri,
                code);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<KaKaoTokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, KaKaoTokenResponse.class);
        return response.getBody();
    }

    public KakaoUserInfoDto getUserId(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(kakaoAccessToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(userInfoUrl, HttpMethod.POST, httpEntity, KakaoUserInfoDto.class);
        return response.getBody();
    }
}
