package com.polar_moviechart.edgeservice.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/edge/users")
public class UserController {

    private final RestTemplate restTemplate;
    private final String USER_SERVICE_URL = "http://localhost:8082/api/v1/users";

//    @PostMapping("/login/kakao")
//    public ResponseEntity<CustomResponse<TokenResponse>> kakaoLogin(@RequestBody KakaoUserInfoDto kakaoUserInfoDto) {
//        return restTemplate.exchange(
//                USER_SERVICE_URL + "/login/kakao",
//                HttpMethod.POST,
//                new HttpEntity<>(kakaoUserInfoDto),
//                new ParameterizedTypeReference<>() {});
//    }
}
