package com.main.writeRoom.service.RoomService;

import com.main.writeRoom.domain.Room;

public interface RoomQueryService {
    Room findRoom(Long roomId);
}
