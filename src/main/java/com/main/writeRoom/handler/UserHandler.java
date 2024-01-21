package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) { super(errorCode); }
}
