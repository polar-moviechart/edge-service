package com.polar_moviechart.edgeservice.domain.kakao.controller;

import com.polar_moviechart.edgeservice.domain.kakao.service.KaKaoTokenResponse;
import com.polar_moviechart.edgeservice.domain.kakao.service.KakaoTokenService;
import com.polar_moviechart.edgeservice.domain.kakao.service.KakaoUserInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/edge/users/kakao")
public class KakaoAuthController {
    private final KakaoTokenService kakaoTokenService;

    @GetMapping("/login/callback")
    public void getKakaoExternalId(@RequestParam(name = "code") String code,
                                   HttpServletResponse httpResponse) {
        KaKaoTokenResponse kaKaoTokenResponse = kakaoTokenService.getTokenAndRedirectUser(code);
        KakaoUserInfoDto kakaoUserInfoDto = kakaoTokenService.getUserId(kaKaoTokenResponse.getAccess_token());

        String redirectUrl = "http://localhost:3000/kakaoAuth?id=" + kakaoUserInfoDto.getId();
        try {
            httpResponse.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
