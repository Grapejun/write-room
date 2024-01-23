package com.main.writeRoom.service.RoomService;

import com.main.writeRoom.domain.mapping.RoomParticipation;
import org.springframework.data.domain.Page;

public interface RoomCommandService {
    Page<RoomParticipation> getMyRoomResultList(Long userId, Integer page);
}
