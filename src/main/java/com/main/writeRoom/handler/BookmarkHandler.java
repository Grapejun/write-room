package com.main.writeRoom.handler;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.handler.GeneralException;

public class BookmarkHandler extends GeneralException {
    public BookmarkHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
