package com.main.writeRoom.service.RoomParticipationService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.repository.RoomPaticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomParticipationServiceImpl implements RoomParticipationService {
    private final RoomPaticipationRepository roomPaticipationRepository;

    @Transactional
    public void leaveRoom(Room room, User user) {
        roomPaticipationRepository.deleteByRoomAndUser(room, user);
    }
}
