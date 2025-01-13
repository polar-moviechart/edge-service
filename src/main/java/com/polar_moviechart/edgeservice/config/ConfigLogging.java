package com.polar_moviechart.edgeservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ConfigLogging implements CommandLineRunner {

    @Value("${cors.origins}")
    private String origins;

    @Value("$cors.allow}")
    private List<String> allow;

    @Override
    public void run(String... args) throws Exception {
        allow.add(origins);
        log.info("Allowed CORS: {}", allow);
    }
}