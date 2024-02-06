package com.main.writeRoom.web.dto.challenge;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDTO {

    //1. 챌린지 루틴, 목표량 생성 결과 dto
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateChallengeResultDTO {
        Long challengeId;
    }

    //2. 챌린지 루틴 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeRoutineDTO {

        Long challengeId;    //챌린지 루틴 식별자
        String userName;     //회원명
        List<UserDTO> userList; //챌린지 참여자 목록
        LocalDate startDate; //시작 날짜
        LocalDate deadline;  //마감 날짜
        Integer targetCount; //목표 일수
        List<NoteDTO> noteList; //챌린지 기간동안 작성한 노트들의 인덱스와 날짜 목록
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        Long userId;
        String userName;
        String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteDTO {
        Long noteId;
        LocalDate date;
    }

    //3. 챌린지 루틴 조회 - 날짜, 200자 이상에 해당하는 노트 목록
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomResultByDate{ //룸의 노트 중 해당 날짜의 200자 이상인 노트를 조회
        Long roomId;
        String roomTitle;
        String roomIntroduction;
        List<NoteResponseDTO.NoteList> noteList;
    }

    //5. 챌린지 루틴, 목표량 포기 결과 dto
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GiveUpChallengeResultDTO {
        Long userId;             //포기한 회원
        Long challengeId;        //포기한 챌린지
        ChallengeStatus status;  //포기 상태
        LocalDateTime createdAt; //포기 시간
    }

    //챌린지 목표량 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeGoalsDTO {

        Long challengeId;    //챌린지 목표량 식별자
        String userName;     //회원명
        Integer achieveCount; //기간 동안의 200자 이상인 노트의 수
        List<UserDTO> userList; //챌린지 참여자 목록
        LocalDate startDate; //시작 날짜
        LocalDate deadline;  //마감 날짜
        Integer targetCount; //목표 일수
    }

    //나의 챌린지
    //나의 챌린지 이력 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyChallengeListDTO {
        List<MyChallengeDTO> myChallengeRoutineDTOList; //챌린지 루틴 이력
        List<MyChallengeDTO> myChallengeGoalsDTOList;   //챌린지 목표량 이력
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyChallengeDTO { //응답 결과의 일부

        Long challengeId; //챌린지 식별자
        String challengeName; //챌린지명
        List<UserDTO> participantList; //참여자 목록 - ChallengeRoutine, ChallengeGoals
        LocalDate endDate; //챌린지 종료일자 - ChallengeRoutineParticipation, ChallengeGoalsParticipation
        ChallengeStatus status; //챌린지 상태(성공 or 실패) - ChallengeRoutineParticipation, ChallengeGoalsParticipation
    }

    //나의 챌린지 상세 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyChallengeRoutineDTO {

        Long challengeId;    //챌린지 루틴 식별자
        String userName;     //회원명
        List<UserDTO> userList; //챌린지 참여자 목록
        LocalDate startDate; //시작 날짜
        LocalDate deadline;  //마감 날짜
        Integer targetCount; //목표 일수
        List<NoteDTO> noteList; //챌린지 기간동안 작성한 노트들의 인덱스와 날짜 목록
        ChallengeStatus challengeStatus; //챌린지 성공여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyChallengeGoalsDTO {

        Long challengeId;    //챌린지 목표량 식별자
        String userName;     //회원명
        Integer achieveCount; //기간 동안의 200자 이상인 노트의 수
        List<UserDTO> userList; //챌린지 참여자 목록
        LocalDate startDate; //시작 날짜
        LocalDate deadline;  //마감 날짜
        Integer targetCount; //목표 일수
        ChallengeStatus challengeStatus; //챌린지 성공 여부
    }
}
