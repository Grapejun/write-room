package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class TokenHandler extends GeneralException {
    public TokenHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
