package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class RoomParticipationHandler extends GeneralException {
    public RoomParticipationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
