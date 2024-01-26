package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class NoteHandler extends GeneralException {
    public NoteHandler(BaseErrorCode errorCode) { super(errorCode);}
}
