package com.main.writeRoom.service.RoomService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.handler.RoomHandler;
import com.main.writeRoom.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomQueryServiceImpl implements RoomQueryService {

    private final RoomRepository roomRepository;

    public Room findRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomHandler(ErrorStatus.ROOM_NOT_FOUND));
    }
}
