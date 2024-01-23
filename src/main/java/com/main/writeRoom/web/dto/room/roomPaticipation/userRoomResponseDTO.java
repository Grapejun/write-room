package com.main.writeRoom.web.dto.room.roomPaticipation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class userRoomResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userRoomInfoList {
        Long userId;
        String profileImg;
        String name;
    }
}
