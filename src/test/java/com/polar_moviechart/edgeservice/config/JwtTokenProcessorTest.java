package com.polar_moviechart.edgeservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
class JwtTokenProcessorTest {

    @Autowired private JwtTokenProcessor jwtTokenProcessor;

    @DisplayName("jwt 토큰 만료 기간 확인")
    @Test
    void checkJwtExpirationPeriod() {
        // given
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MzE1NTU0MTIsImV4cCI6MTczMjE2MDIxMn0.s7KhM7kLK5je8b8V_UCT3U3Sde2_RfOyE9V94dyNqrA";

        // when
        Date expiration = null;
        try {
            Claims claims = jwtTokenProcessor.getClaims(jwtToken);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException e) {
            expiration = e.getClaims().getExpiration();
        }
        LocalDateTime localDateTimeExpidation = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());

        // then
        System.out.println("=== jwt Expiration = " + localDateTimeExpidation);
    }
}