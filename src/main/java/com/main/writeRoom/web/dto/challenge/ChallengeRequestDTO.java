package com.main.writeRoom.web.dto.challenge;

import com.main.writeRoom.domain.User.User;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class ChallengeRequestDTO {

    //1. 챌린지 루틴 생성
    @Getter
    public static class ChallengeRoutineDTO {

        @NotNull(message = "참여자가 존재해야 합니다.")
        List<Long> userList; //참여자 목록
        @Max(value = 7, message = "최대 7일까지만 가능합니다.")
        @Min(value = 1, message = "최소 1일부터 가능합니다.")
        Integer targetCount; //목표 일수
        @NotNull(message = "시작날짜가 존재해야 합니다.")
        LocalDate startDate; //시작 날짜
        @NotNull(message = "마감날짜가 존재해야 합니다.")
        LocalDate deadline;  //마감 날짜
    }

    //2. 챌린지 루틴 조회
    //3. 챌린지 루틴 조회 - 스탬프 클릭
    //4. 챌린지 루틴 조회 - 참여자 클릭
    //5. 챌린지 루틴 포기
}
