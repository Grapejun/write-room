package com.main.writeRoom.web.dto.room.roomPaticipation;

import com.main.writeRoom.domain.mapping.Authority;
import java.util.List;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getUserRoom {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Long userId;
        String name;
        Authority authority;
        List<getUserRoomList> userRoomLists;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getUserRoomList {
        Long userId;
        String profileImg;
        String name;
        Authority authority;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getUpdatedAtUserList {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Long userId;
        String name;
        String profileImg;
        String updateAt;
    }
}
