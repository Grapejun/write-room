package com.main.writeRoom.service.RoomParticipationService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;

public interface RoomParticipationService {
    void leaveRoom(Room room, User user);
}
