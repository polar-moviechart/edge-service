package com.polar_moviechart.edgeservice.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternMatcherTest {

    private final PatternMatcher patternMatcher = new PatternMatcher();

    @DisplayName("")
    @Test
    void isPublicUrlTest() {
        // given
        String publicUrl = "/user-service/public/api/v1/test";
        String nonPublicUrl = "/user-service/secure/api/v1/test";

        // when // then
        assertTrue(patternMatcher.isPublicUrl(publicUrl));
        assertFalse(patternMatcher.isPublicUrl(nonPublicUrl));
    }
}
