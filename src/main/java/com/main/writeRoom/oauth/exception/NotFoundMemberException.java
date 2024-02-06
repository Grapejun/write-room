package com.main.writeRoom.oauth.exception;

import lombok.NoArgsConstructor;


public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException() {
        super(ExceptionContext.NOT_FOUND_MEMBER);
    }
}

