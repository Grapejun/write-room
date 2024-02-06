package com.main.writeRoom.oauth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(ExceptionContext exceptionContext) {
        super(HttpStatus.NOT_FOUND, exceptionContext.getCode(), exceptionContext.getMessage());
    }
}