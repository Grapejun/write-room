package com.main.writeRoom.web.dto.challenge;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.validation.annotation.IsStartDateToday;
import com.main.writeRoom.validation.annotation.IsStartDateTodayNull;
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
        @IsStartDateToday
        LocalDate startDate; //시작 날짜
        @NotNull(message = "마감날짜가 존재해야 합니다.")
        LocalDate deadline;  //마감 날짜
    }


    //챌린지 목표량
    //챌린지 목표량 생성
    @Getter
    public static class ChallengeGoalsDTO {

        @NotNull(message = "참여자가 존재해야 합니다.")
        List<Long> userList; //참여자 목록
        @Max(value = 1000, message = "최대 7일까지만 가능합니다.")
        @Min(value = 1, message = "최소 1일부터 가능합니다.")
        Integer targetCount; //목표 노트 개수
        @IsStartDateTodayNull
        LocalDate startDate; //시작 날짜 -> null 가능
        LocalDate deadline;  //마감 날짜 -> null 가능
    }
}
