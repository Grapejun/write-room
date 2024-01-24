package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChallengeConverter {

    //1. 챌린지 루틴 생성
    public static ChallengeRoutine toChallengeRoutine(Room room, ChallengeRequestDTO.ChallengeRoutineDTO request) {
        return ChallengeRoutine.builder()
                .deadline(request.getDeadline())
                .startDate(request.getStartDate())
                .targetCount(request.getTargetCount())
                .room(room)
                .challengeRoutineParticipationList(new ArrayList<>())
                .build();
    }

    public static ChallengeResponseDTO.CreateChallengeRoutineResultDTO toCreateChallengeRoutineResultDTO(ChallengeRoutine challengeRoutine) {
        return ChallengeResponseDTO.CreateChallengeRoutineResultDTO.builder()
                .challengeRoutineId(challengeRoutine.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //2. 챌린지 루틴 조회
    public static ChallengeResponseDTO.RoutineNoteDTO toRoutineNoteDTO(Note note) {
        return null;
    }

    public static ChallengeResponseDTO.ChallengeRoutineDTO toChallengeRoutineDTO(List<ChallengeResponseDTO.RoutineNoteDTO> noteList, ChallengeRoutine challengeRoutine) {

        return ChallengeResponseDTO.ChallengeRoutineDTO.builder()
                .startDate(challengeRoutine.getStartDate())
                .deadline(challengeRoutine.getDeadline())
                .targetCount(challengeRoutine.getTargetCount())
                .build();
    }

    //3. 챌린지 루틴 조회 - 스탬프 클릭

    //5. 챌린지 루틴 포기
    public static ChallengeResponseDTO.GiveUpChallengeRoutineResultDTO toGiveUpChallengeRoutineResultDTO(ChallengeRoutineParticipation routineParticipation) {

        return ChallengeResponseDTO.GiveUpChallengeRoutineResultDTO.builder()
                .userId(routineParticipation.getUser().getId())
                .challengeRoutineId(routineParticipation.getChallengeRoutine().getId())
                .status(routineParticipation.getChallengeStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
