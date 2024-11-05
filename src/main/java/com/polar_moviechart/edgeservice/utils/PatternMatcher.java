package com.polar_moviechart.edgeservice.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PatternMatcher {
    private static final Pattern PUBLIC_URL_PATTERN = Pattern.compile("^/[^/]+/public");

    public boolean isPublicUrl(String url) {
        return PUBLIC_URL_PATTERN.matcher(url).find();
    }
}
