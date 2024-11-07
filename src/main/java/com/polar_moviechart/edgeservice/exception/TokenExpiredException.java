package com.polar_moviechart.edgeservice.exception;

public class TokenExpiredException extends RuntimeException {
    private static final ErrorInfo errorInfo = ErrorInfo.DEFAULT_ERROR;

    public TokenExpiredException() {
        super(errorInfo.getMessage());
    }

    public TokenExpiredException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getMessage(), cause);
    }

    public TokenExpiredException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
    }

    public TokenExpiredException(Throwable cause) {
        super(errorInfo.getMessage(), cause);
    }
}
