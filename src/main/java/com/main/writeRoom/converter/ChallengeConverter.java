package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static ChallengeResponseDTO.ChallengeRoutineDTO toChallengeRoutineDTO(User user, ChallengeRoutine routine, List<ChallengeResponseDTO.NoteDTO> noteList)  {
        List<ChallengeResponseDTO.UserDTO> userList = routine.getChallengeRoutineParticipationList().stream()
                .map(participation -> {
                    return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.ChallengeRoutineDTO.builder()
                .userName(user.getName())
                .startDate(routine.getStartDate())
                .deadline(routine.getDeadline())
                .targetCount(routine.getTargetCount())
                .userList(userList)
                .noteList(noteList)
                .build();
    }

    public static ChallengeResponseDTO.UserDTO toUserDTO(User user) {
        return ChallengeResponseDTO.UserDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static ChallengeResponseDTO.NoteDTO toNoteDTO(Note note) {
        return ChallengeResponseDTO.NoteDTO.builder()
                .noteId(note.getId())
                .date(note.getCreatedAt().toLocalDate())
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
