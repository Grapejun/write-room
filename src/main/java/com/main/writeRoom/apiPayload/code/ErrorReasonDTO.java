package com.main.writeRoom.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDTO {

    private HttpStatus httpStatus;

    private final boolean isFailure;
    private final String code;
    private final String message;

    public boolean getIsFailure(){return isFailure;}
}
