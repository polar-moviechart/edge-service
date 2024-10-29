package com.polar_moviechart.edgeservice.utils;

import com.polar_moviechart.edgeservice.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class CustomResponse<T> {
    private Boolean isSuccess = true;
    private String ErrorMsg = null;
    private String code = null;
    private Optional<T> data;

    public CustomResponse(T data) {
        this.data = Optional.ofNullable(data);
    }

    public void setCode(ErrorCode errorCode) {
        this.ErrorMsg = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public void setCode(String errorCode) {
        this.code = errorCode;
    }
}
