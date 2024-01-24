package com.main.writeRoom.web.dto.challenge;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDTO {

    //1. 챌린지 루틴 생성
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateChallengeRoutineResultDTO {
        Long challengeRoutineId;
        LocalDateTime createdAt;
    }

    //2. 챌린지 루틴 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeRoutineDTO {
        List<RoutineNoteDTO> routineNoteDTOListnoteList; //챌린지 루틴 기간동안 작성한 노트와 작성 날짜들의 리스트
        List<Long> userList; //챌린지 참여자 목록
        LocalDate startDate; //시작 날짜
        LocalDate deadline;  //마감 날짜
        Integer targetCount; //목표 일수
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineNoteDTO {
        Note note; //챌린지 루틴 때 쓴 노트
        LocalDate writeDate; //노트 작성 날짜
    }

    //3. 챌린지 루틴 조회 - 스탬프 클릭
    //4. 챌린지 루틴 조회 - 참여자 클릭

    //5. 챌린지 루틴 포기
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GiveUpChallengeRoutineResultDTO {
        Long challengeRoutineId;
        LocalDateTime createdAt; //포기 시간
    }

}
