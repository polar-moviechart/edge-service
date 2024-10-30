package com.polar_moviechart.edgeservice.config;

import com.polar_moviechart.edgeservice.filter.JwtAuthFilter;
import com.polar_moviechart.edgeservice.filter.JwtAuthFilterOptional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthFilterOptional jwtAuthFilterOptional;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/**").authenticated() // 인증 필요
                        .requestMatchers("/api/v1/movies/**").permitAll() // 인증 필요 없음
                        .requestMatchers("/api/v1/users/login/kakao").permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // JwtAuthFilter 추가
                .addFilterAfter(jwtAuthFilterOptional, JwtAuthFilter.class); // JwtAuthFilterOptional 추가

        return http.build();
    }
}
