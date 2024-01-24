package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class RoomHandler extends GeneralException {

    public RoomHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
