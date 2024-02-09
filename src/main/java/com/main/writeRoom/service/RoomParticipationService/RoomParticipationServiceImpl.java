package com.main.writeRoom.service.RoomParticipationService;

import static com.main.writeRoom.domain.mapping.Authority.MANAGER;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.handler.RoomHandler;
import com.main.writeRoom.repository.RoomParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomParticipationServiceImpl implements RoomParticipationService {
    private final RoomParticipationRepository roomParticipationRepository;

    @Transactional
    public void leaveRoom(Room room, User user) {
        roomParticipationRepository.deleteByRoomAndUser(room, user);
    }

    @Transactional
    public void outRoom(Room room, User user, User outUser) {
        RoomParticipation roomParticipation = roomParticipationRepository.findByRoomAndUser(room, user);
        if (roomParticipation.getAuthority() != MANAGER) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }
        roomParticipationRepository.deleteByRoomAndUser(room, outUser);
    }

    @Transactional
    public void updateAuthority(Room room, User user, User updateUser, String authority) {
        RoomParticipation roomParticipation = roomParticipationRepository.findByRoomAndUser(room, user);
        if (roomParticipation.getAuthority() != MANAGER) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }
        RoomParticipation updateRoom = roomParticipationRepository.findByRoomAndUser(room, updateUser);
        updateRoom.updateAuthority(authority);
    }

    public List<User> findRoomUserList(Room room) {
        List<User> userList = roomParticipationRepository.findByRoom(room).stream()
                .map(roomParticipation -> roomParticipation.getUser())
                .collect(Collectors.toList());
        return userList;
    }
}
