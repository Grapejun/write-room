package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
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

    public static ChallengeResponseDTO.CreateChallengeResultDTO toCreateChallengeRoutineResultDTO(ChallengeRoutine challengeRoutine) {
        return ChallengeResponseDTO.CreateChallengeResultDTO.builder()
                .challengeId(challengeRoutine.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //2. 챌린지 루틴 조회
    public static ChallengeResponseDTO.ChallengeRoutineDTO toChallengeRoutineDTO(User user, ChallengeRoutine routine, List<ChallengeResponseDTO.NoteDTO> noteList)  {
        List<ChallengeResponseDTO.UserDTO> userList = routine.getChallengeRoutineParticipationList().stream()
                .filter(participation -> (participation.getChallengeStatus() == ChallengeStatus.PROGRESS) || (participation.getChallengeStatus() == ChallengeStatus.SUCCESS))
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

    //5. 챌린지 루틴 포기
    public static ChallengeResponseDTO.GiveUpChallengeResultDTO toGiveUpChallengeRoutineResultDTO(ChallengeRoutineParticipation routineParticipation) {

        return ChallengeResponseDTO.GiveUpChallengeResultDTO.builder()
                .userId(routineParticipation.getUser().getId())
                .challengeId(routineParticipation.getChallengeRoutine().getId())
                .status(routineParticipation.getChallengeStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //챌린지 목표량
    //챌린지 목표량 생성
    public static ChallengeGoals toChallengeGoals(Room room, ChallengeRequestDTO.ChallengeGoalsDTO request) {
        return ChallengeGoals.builder()
                .deadline(request.getDeadline())
                .startDate(request.getStartDate())
                .targetCount(request.getTargetCount())
                .room(room)
                .challengeGoalsParticipationList(new ArrayList<>())
                .build();
    }

    public static ChallengeResponseDTO.CreateChallengeResultDTO toCreateChallengeGoalsResultDTO(ChallengeGoals challengeGoals) {
        return ChallengeResponseDTO.CreateChallengeResultDTO.builder()
                .challengeId(challengeGoals.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //챌린지 목표량 조회
    public static ChallengeResponseDTO.ChallengeGoalsDTO toChallengeGoalsDTO(User user, ChallengeGoals goals, Integer achieveCount)  {
        List<ChallengeResponseDTO.UserDTO> userList = goals.getChallengeGoalsParticipationList().stream()
                .filter(participation -> (participation.getChallengeStatus() == ChallengeStatus.PROGRESS) || (participation.getChallengeStatus() == ChallengeStatus.SUCCESS))
                .map(participation -> {
                    return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.ChallengeGoalsDTO.builder()
                .userName(user.getName())
                .startDate(goals.getStartDate())
                .deadline(goals.getDeadline())
                .targetCount(goals.getTargetCount())
                .userList(userList)
                .achieveCount(achieveCount)
                .build();
    }


    //챌린지 목표량 포기
    public static ChallengeResponseDTO.GiveUpChallengeResultDTO toGiveUpChallengeGoalsResultDTO(ChallengeGoalsParticipation goalsParticipation) {

        return ChallengeResponseDTO.GiveUpChallengeResultDTO.builder()
                .userId(goalsParticipation.getUser().getId())
                .challengeId(goalsParticipation.getChallengeGoals().getId())
                .status(goalsParticipation.getChallengeStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
