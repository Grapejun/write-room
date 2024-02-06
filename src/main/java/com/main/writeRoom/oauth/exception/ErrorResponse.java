package com.main.writeRoom.oauth.exception;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(CustomException customException) {
        return ErrorResponse.builder()
                .code(customException.getCode())
                .message(customException.getMessage())
                .build();
    }
}
