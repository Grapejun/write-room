package com.main.writeRoom.oauth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionContext {

    NOT_FOUND_MEMBER(1000, "존재하지 않는 멤버입니다.");

    private final int code;
    private final String message;
}
