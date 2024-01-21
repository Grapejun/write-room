package com.main.writeRoom.web.dto.room;

import com.main.writeRoom.web.dto.room.roomPaticipation.userRoomResponseDTO.userRoomInfoList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyRoomResultDto {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Long roomId;
        String roomTitle;
        String updatedAt;
        Long totalMember;
        List<userRoomInfoList> userRoomList;
    }
}
