package com.polar_moviechart.edgeservice.exception;

public class TokenProcessException extends RuntimeException {
    private static final String msg = "토큰 파싱 중 문제가 발생했습니다.";

    public TokenProcessException() {
        super(msg);
    }

    public TokenProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenProcessException(String message) {
        super(message);
    }

    public TokenProcessException(Throwable cause) {
        super(msg, cause);
    }
}
