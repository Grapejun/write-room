package com.main.writeRoom.converter;

import com.main.writeRoom.domain.ACHIEVE;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
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
                .build();
    }

    //2. 챌린지 루틴 조회
    public static ChallengeResponseDTO.ChallengeRoutineDTO toChallengeRoutineDTO(User user, ChallengeRoutine routine, List<ChallengeResponseDTO.NoteDTO> noteList)  {
        List<ChallengeResponseDTO.UserDTO> userList = routine.getChallengeRoutineParticipationList().stream()
                .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                .map(participation -> {
                        return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.ChallengeRoutineDTO.builder()
                .challengeId(routine.getId())
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

    //챌린지 루틴 해당 날짜의 200자 이상 노트 목록 조회
    public static ChallengeResponseDTO.RoomResultByDate toRoomResultByDate(Room room, Page<Note> notes, LocalDate date) {
        List<NoteResponseDTO.NoteList> toRoomResultNoteDTOList = notes.stream()
                .filter(note -> note.getCreatedAt().toLocalDate().isEqual(date))
                .filter(note -> note.getAchieve() == ACHIEVE.TRUE)
                .map(NoteConverter::toRoomResultNoteDTOList).collect(Collectors.toList());

        return ChallengeResponseDTO.RoomResultByDate.builder()
                .roomId(room.getId())
                .noteList(toRoomResultNoteDTOList)
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
                .build();
    }

    //챌린지 목표량 조회
    public static ChallengeResponseDTO.ChallengeGoalsDTO toChallengeGoalsDTO(User user, ChallengeGoals goals, Integer achieveCount)  {
        List<ChallengeResponseDTO.UserDTO> userList = goals.getChallengeGoalsParticipationList().stream()
                .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                .map(participation -> {
                    return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.ChallengeGoalsDTO.builder()
                .challengeId(goals.getId())
                .userName(user.getName())
                .startDate(goals.getStartDate())
                .deadline(goals.getDeadline())
                .targetCount(goals.getTargetCount())
                .userList(userList)
                .achieveCount(achieveCount)
                .build();
    }


    //나의 챌린지
    //나의 챌린지 이력 조회
    public static ChallengeResponseDTO.MyChallengeDTO toMyChallengeDTO(ChallengeRoutine routine, ChallengeRoutineParticipation routineParticipation) {
        List<ChallengeResponseDTO.UserDTO> participantList = routine.getChallengeRoutineParticipationList().stream()
                .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                .map(participant -> ChallengeConverter.toUserDTO(participant.getUser())).collect(Collectors.toList());

        LocalDate endDate;
        if (routineParticipation.getChallengeStatus() == ChallengeStatus.GIVEUP) {
            endDate = routineParticipation.getStatusUpdatedAt();
        }
        else endDate = routine.getDeadline();

        return ChallengeResponseDTO.MyChallengeDTO.builder()
                .challengeId(routine.getId())
                .challengeName(routine.toString())
                .participantList(participantList)
                .status(routineParticipation.getChallengeStatus())
                .endDate(endDate)
                .build();
    }

    public static ChallengeResponseDTO.MyChallengeDTO toMyChallengeDTO(ChallengeGoals goals, ChallengeGoalsParticipation goalsParticipation) {
        List<ChallengeResponseDTO.UserDTO> participantList = goals.getChallengeGoalsParticipationList().stream()
                    .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                    .map(participant -> ChallengeConverter.toUserDTO(participant.getUser())).collect(Collectors.toList());

        LocalDate endDate;
        if (goalsParticipation.getChallengeStatus() == ChallengeStatus.GIVEUP) {
            endDate = goalsParticipation.getStatusUpdatedAt();
        }
        else endDate = goals.getDeadline();

        return ChallengeResponseDTO.MyChallengeDTO.builder()
                .challengeId(goals.getId())
                .challengeName(goals.toString())
                .participantList(participantList)
                .status(goalsParticipation.getChallengeStatus())
                .endDate(endDate)
                .build();
    }

    public static ChallengeResponseDTO.MyChallengeListDTO toMyChallengeDTOList(List<ChallengeResponseDTO.MyChallengeDTO> myRoutineList,List<ChallengeResponseDTO.MyChallengeDTO> myGoalsList) {

        return ChallengeResponseDTO.MyChallengeListDTO.builder()
                .myChallengeRoutineDTOList(myRoutineList)
                .myChallengeGoalsDTOList(myGoalsList)
                .build();
    }

    //나의 챌린지 상세 조회 - 루틴
    public static ChallengeResponseDTO.MyChallengeRoutineDTO toMyChallengeRoutineDTO(User user, ChallengeRoutine routine, List<ChallengeResponseDTO.NoteDTO> noteList, ChallengeRoutineParticipation routineParticipation)  {
        List<ChallengeResponseDTO.UserDTO> userList = routine.getChallengeRoutineParticipationList().stream()
                .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                .map(participation -> {
                    return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.MyChallengeRoutineDTO.builder()
                .challengeId(routine.getId())
                .userName(user.getName())
                .startDate(routine.getStartDate())
                .deadline(routine.getDeadline())
                .targetCount(routine.getTargetCount())
                .userList(userList)
                .noteList(noteList)
                .challengeStatus(routineParticipation.getChallengeStatus())
                .build();
    }

    //나의 챌린지 상세 조회 - 목표량
    public static ChallengeResponseDTO.MyChallengeGoalsDTO toMyChallengeGoalsDTO(User user, ChallengeGoals goals, Integer achieveCount, ChallengeGoalsParticipation goalsParticipation)  {
        List<ChallengeResponseDTO.UserDTO> userList = goals.getChallengeGoalsParticipationList().stream()
                .filter(participation -> participation.getChallengeStatus() != ChallengeStatus.GIVEUP)
                .map(participation -> {
                    return ChallengeConverter.toUserDTO(participation.getUser());
                }).collect(Collectors.toList());

        return ChallengeResponseDTO.MyChallengeGoalsDTO.builder()
                .challengeId(goals.getId())
                .userName(user.getName())
                .startDate(goals.getStartDate())
                .deadline(goals.getDeadline())
                .targetCount(goals.getTargetCount())
                .userList(userList)
                .achieveCount(achieveCount)
                .challengeStatus(goalsParticipation.getChallengeStatus())
                .build();
    }

    //참여가능한 회원 목록 dto로 변환
    public static ChallengeResponseDTO.UserList toUserList(List<ChallengeResponseDTO.UserDTO> userDTOList) {
        return ChallengeResponseDTO.UserList.builder()
                .userList(userDTOList)
                .build();
    }
}
