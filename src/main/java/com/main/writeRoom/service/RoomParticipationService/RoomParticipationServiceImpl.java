package com.main.writeRoom.service.RoomParticipationService;

import static com.main.writeRoom.domain.mapping.Authority.MANAGER;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.handler.RoomHandler;
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

    @Transactional
    public void outRoom(Room room, User user, User outUser) {
        RoomParticipation roomParticipation = roomPaticipationRepository.findByRoomAndUser(room, user);
        if (roomParticipation.getAuthority() != MANAGER) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }
        roomPaticipationRepository.deleteByRoomAndUser(room, outUser);
    }

    @Transactional
    public void updateAuthority(Room room, User user, User updateUser, String authority) {
        RoomParticipation roomParticipation = roomPaticipationRepository.findByRoomAndUser(room, user);
        if (roomParticipation.getAuthority() != MANAGER) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }
        RoomParticipation updateRoom = roomPaticipationRepository.findByRoomAndUser(room, updateUser);
        updateRoom.updateAuthority(authority);
    }
}
