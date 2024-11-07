package com.polar_moviechart.edgeservice.exception;

import com.polar_moviechart.edgeservice.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.polar_moviechart.userservice.domain.controller"})
public class GlobalControllerAdvice {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<CustomResponse<Object>> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomResponse<>(ErrorInfo.DEFAULT_ERROR));
    }

    @ExceptionHandler(value = {TokenProcessException.class})
    public ResponseEntity<CustomResponse<Object>> handleTokenCreationException(TokenProcessException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomResponse<>(ErrorInfo.TOKEN_CREATION_ERROR));
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<CustomResponse<Object>> handleTokenExpiredException(TokenExpiredException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomResponse<>(ErrorInfo.TOKEN_CREATION_ERROR));
    }
}
