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
        String roomImg;
        Long totalMember;
        List<userRoomInfoList> userRoomList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyRoomAllResultDto {
        Long roomId;
        String roomTitle;
        String updatedAt;
        String roomImg;
        Long totalMember;
        List<userRoomInfoList> userRoomList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomInfoResult {
        Long roomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyRoomInfoResult {
        Long roomId;
        String title;
        String introduction;
        String coverImg;
    }

    //챌린지 달성률 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeAchieveResult {
        Long routineId;
        Integer routineTargetCount; //목표일수
        Double routineAchieveRate; //달성률(퍼센트)

        Long goalsId;
        Integer goalsTargetCount;  //목표량
        Double goalsAchieveRate;  //달성률(퍼센트)
    }

}
