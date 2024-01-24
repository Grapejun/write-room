package com.main.writeRoom.service.RoomService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.web.dto.room.RoomRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface RoomCommandService {
    Page<RoomParticipation> getMyRoomResultList(Long userId, Integer page);
    Page<RoomParticipation> getUserRoomInfoList(Room room);
    RoomParticipation getUserRoomInfo(Room room, User user);
    Room createRoom(User user, RoomRequestDTO.CreateRoomDTO request, MultipartFile roomImg);
    void deleteRoom(Room room, User user);
}
