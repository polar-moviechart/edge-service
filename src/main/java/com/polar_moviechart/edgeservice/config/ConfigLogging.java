package com.polar_moviechart.edgeservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public abstract class ConfigLogging {

    @Value("{cors.origins}")
    private static String origins;
    
    @PostConstruct
    public void logOrigins() {
        System.out.println("Allowed CORS Origins: " + origins);
    }
}
