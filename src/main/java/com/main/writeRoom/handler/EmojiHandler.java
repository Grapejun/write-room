package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class EmojiHandler extends GeneralException {
    public EmojiHandler(BaseErrorCode errorCode) { super(errorCode); }
}
