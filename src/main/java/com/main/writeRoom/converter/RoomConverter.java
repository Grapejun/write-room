package com.main.writeRoom.converter;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.web.dto.room.RoomResponseDTO;
import com.main.writeRoom.web.dto.room.roomPaticipation.userRoomResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class RoomConverter {
    public static List<RoomResponseDTO.MyRoomResultDto> myRoomListInfoDTO(Page<RoomParticipation> rooms) {
        return rooms.stream()
                .map(room -> {
                    long totalMemberCount = room.getRoom().getRoomParticipations().size();
                    return RoomResponseDTO.MyRoomResultDto.builder()
                            .roomId(room.getRoom().getId())
                            .roomTitle(room.getRoom().getTitle())
                            .updatedAt(room.getRoom().daysSinceLastUpdate())
                            .userRoomList(room.getRoom().getRoomParticipations().stream()
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
                .profileImg(user.getProfileImg())
                .build();
    }
}
