package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.Authority;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.web.dto.room.RoomRequestDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO;
import com.main.writeRoom.web.dto.room.roomPaticipation.userRoomResponseDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public class RoomConverter {
    public static List<RoomResponseDTO.MyRoomResultDto> myRoomListInfoDTO(Page<RoomParticipation> rooms) {
        return rooms.stream()
                .map(room -> {
                    long totalMemberCount = room.getRoom().getRoomParticipationList().size();
                    return RoomResponseDTO.MyRoomResultDto.builder()
                            .roomId(room.getRoom().getId())
                            .roomTitle(room.getRoom().getTitle())
                            .roomImg(room.getRoom().getCoverImg())
                            .updatedAt(room.getRoom().daysSinceLastUpdate())
                            .userRoomList(room.getRoom().getRoomParticipationList().stream()
                                    .limit(3)
                                    .map(roomParticipation -> userRoomInfoDTO(roomParticipation.getUser()))
                                    .collect(Collectors.toList()))
                            .totalMember(totalMemberCount)
                            .isLast(rooms.isLast())
                            .isFirst(rooms.isFirst())
                            .totalPage(rooms.getTotalPages())
                            .totalElements(rooms.getTotalElements())
                            .listSize(rooms.getSize())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static userRoomResponseDTO.userRoomInfoList userRoomInfoDTO(User user) {
        return userRoomResponseDTO.userRoomInfoList.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImg(user.getProfileImage())
                .build();
    }

    public static userRoomResponseDTO.getUserRoom toUserRoomResultDTO(RoomParticipation roomParticipation,
                                                                      Page<RoomParticipation> roomParticipations) {
        List<userRoomResponseDTO.getUserRoomList> toUserRoomResultDTOList = roomParticipations.stream()
                .map(userRoom -> userRoomInfoListDTO(userRoom.getUser(), userRoom)).collect(Collectors.toList());

        return userRoomResponseDTO.getUserRoom.builder()
                .isFirst(roomParticipations.isFirst())
                .isLast(roomParticipations.isLast())
                .totalPage(roomParticipations.getTotalPages())
                .totalElements(roomParticipations.getTotalElements())
                .listSize(roomParticipations.getSize())
                .userId(roomParticipation.getUser().getId())
                .name(roomParticipation.getUser().getName())
                .authority(roomParticipation.getAuthority())
                .userRoomLists(toUserRoomResultDTOList)
                .build();
    }

    public static userRoomResponseDTO.getUserRoomList userRoomInfoListDTO(User user, RoomParticipation userRoom) {
        return userRoomResponseDTO.getUserRoomList.builder()
                .userId(user.getId())
                .profileImg(user.getProfileImage())
                .name(user.getName())
                .authority(userRoom.getAuthority())
                .build();
    }

    public static Room toRoom(RoomRequestDTO.CreateRoomDTO request, String imgUrl) {
        return Room.builder()
                .title(request.getRoomTitle())
                .coverImg(imgUrl)
                .build();
    }

    public static RoomParticipation toUserRoom(Room room, User user) {
        return RoomParticipation.builder()
                .user(user)
                .room(room)
                .authority(Authority.MANAGER)
                .build();
    }

    public static RoomResponseDTO.RoomInfoResult toCreateRoomResultDTO(Room room) {
        return RoomResponseDTO.RoomInfoResult.builder()
                .roomId(room.getId())
                .build();
    }

    public static List<userRoomResponseDTO.getUpdatedAtUserList> updateAtUserList(
            Page<RoomParticipation> roomParticipations) {
        return roomParticipations.stream()
                .map(roomParticipation -> {
                    User user = roomParticipation.getUser();
                    List<Note> userNotes = getNotesForUserInRoom(roomParticipation);
                    List<String> noteUpdateDates = getNoteUpdateDates(userNotes);

                    return userRoomResponseDTO.getUpdatedAtUserList.builder()
                            .userId(user.getId())
                            .profileImg(user.getProfileImage())
                            .name(user.getName())
                            .updateAt(String.join(", ", noteUpdateDates))
                            .isFirst(roomParticipations.isFirst())
                            .isLast(roomParticipations.isLast())
                            .totalPage(roomParticipations.getTotalPages())
                            .totalElements(roomParticipations.getTotalElements())
                            .listSize(roomParticipations.getSize())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static List<Note> getNotesForUserInRoom(RoomParticipation roomParticipation) {
        Room room = roomParticipation.getRoom();
        return room.getNoteList().stream()
                .filter(note -> note.getUser().equals(roomParticipation.getUser()))
                .collect(Collectors.toList());
    }

    private static List<String> getNoteUpdateDates(List<Note> notes) {
        return notes.stream()
                .map(Note::daysSinceLastUpdate)
                .collect(Collectors.toList());
    }

    public static RoomParticipation toUserParticipateIn(Room room, User user) {
        return RoomParticipation.builder()
                .user(user)
                .room(room)
                .authority(Authority.PARTICIPANT)
                .build();
    }

    //챌린지 달성률 조회
    public static RoomResponseDTO.ChallengeAchieveResult toChallengeAchieveResult(ChallengeRoutineParticipation crp, ChallengeGoalsParticipation cgp) {
        double routineAchieveRate = 0.0, goalsAchieveRate = 0.0;
        Long routineId = null, goalsId = null;
        int routineTargetCount = 0, goalsTargetCount = 0;
        if (crp != null) {
            double routineAchieveRate1 = crp.getChallengeRoutine().getTargetCount() * (ChronoUnit.DAYS.between(crp.getChallengeRoutine().getStartDate(), LocalDate.now())/7) + crp.getAchieveCount();
            double routineAchieveRate2 = crp.getChallengeRoutine().getTargetCount() * ((ChronoUnit.DAYS.between(crp.getChallengeRoutine().getStartDate(), crp.getChallengeRoutine().getDeadline())+1)/7);
            routineAchieveRate = Math.round(routineAchieveRate1/routineAchieveRate2 * 1000)/10.0;
            routineId = crp.getChallengeRoutine().getId();
            routineTargetCount = crp.getChallengeRoutine().getTargetCount();
        }

        if (cgp != null) {
            goalsAchieveRate = ((double)cgp.getAchieveCount()/cgp.getChallengeGoals().getTargetCount())*100;
            goalsAchieveRate = Math.round(goalsAchieveRate * 10)/10.0;
            goalsId = cgp.getChallengeGoals().getId();
            goalsTargetCount = cgp.getChallengeGoals().getTargetCount();
        }

        return RoomResponseDTO.ChallengeAchieveResult.builder()
                .routineId(routineId)
                .routineTargetCount(routineTargetCount)
                .routineAchieveRate(routineAchieveRate)
                .goalsId(goalsId)
                .goalsTargetCount(goalsTargetCount)
                .goalsAchieveRate(goalsAchieveRate)
                .build();
    }

    public static List<RoomResponseDTO.MyRoomAllResultDto> myRoomListAllInfoDTO(List<RoomParticipation> roomParticipations) {
        return roomParticipations.stream()
                .map(room -> {
                    long totalMemberCount = room.getRoom().getRoomParticipationList().size();
                    return RoomResponseDTO.MyRoomAllResultDto.builder()
                            .roomId(room.getRoom().getId())
                            .roomTitle(room.getRoom().getTitle())
                            .roomImg(room.getRoom().getCoverImg())
                            .updatedAt(room.getRoom().daysSinceLastUpdate())
                            .userRoomList(room.getRoom().getRoomParticipationList().stream()
                                    .limit(3)
                                    .map(roomParticipation -> userRoomInfoDTO(roomParticipation.getUser()))
                                    .collect(Collectors.toList()))
                            .totalMember(totalMemberCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static RoomResponseDTO.MyRoomInfoResult MyRoomInfoResult(Room room) {
        return RoomResponseDTO.MyRoomInfoResult
                .builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .introduction(room.getIntroduction())
                .coverImg(room.getCoverImg())
                .build();
    }

    public static RoomResponseDTO.MyRoomInfoResultDTO MyRoomInfoResultDTO(Room room, RoomParticipation roomParticipation) {
        return RoomResponseDTO.MyRoomInfoResultDTO
                .builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .introduction(room.getIntroduction())
                .coverImg(room.getCoverImg())
                .authority(roomParticipation.getAuthority().toString())
                .build();
    }
}
