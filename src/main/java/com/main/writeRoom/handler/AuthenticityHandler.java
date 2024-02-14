package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class AuthenticityHandler extends GeneralException {
    public AuthenticityHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
