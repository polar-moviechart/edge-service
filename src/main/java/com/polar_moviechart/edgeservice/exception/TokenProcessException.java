package com.polar_moviechart.edgeservice.exception;

public class TokenProcessException extends RuntimeException {
    private static final ErrorInfo errorInfo = ErrorInfo.DEFAULT_ERROR;

    public TokenProcessException() {
        super(errorInfo.getMessage());
    }

    public TokenProcessException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getMessage(), cause);
    }

    public TokenProcessException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
    }

    public TokenProcessException(Throwable cause) {
        super(errorInfo.getMessage(), cause);
    }
}
