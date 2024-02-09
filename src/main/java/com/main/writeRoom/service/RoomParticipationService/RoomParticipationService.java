package com.main.writeRoom.service.RoomParticipationService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;

import java.util.List;

public interface RoomParticipationService {
    void leaveRoom(Room room, User user);
    void outRoom(Room room, User user, User outUser);
    void updateAuthority(Room room, User user, User updateUser, String authority);
    public List<User> findRoomUserList(Room room);
}
