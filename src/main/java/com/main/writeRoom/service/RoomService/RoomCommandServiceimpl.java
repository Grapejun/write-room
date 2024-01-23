package com.main.writeRoom.service.RoomService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.RoomPaticipationRepository;
import com.main.writeRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomCommandServiceimpl implements RoomCommandService {
    private final RoomPaticipationRepository userRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Page<RoomParticipation> getMyRoomResultList(Long userId, Integer page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("room.updatedAt")));
        return userRoomRepository.findAllByUser(user, pageRequest);
    }

    @Override
    public Page<RoomParticipation> getUserRoomInfoList(Room room) {
        Page<RoomParticipation> userRoomInfoList = userRoomRepository.findAllByRoom(room, PageRequest.of(0, 10));
        return userRoomInfoList;
    }

    @Override
    public RoomParticipation getUserRoomInfo(Room room, User user) {
        RoomParticipation userRoomInfo = userRoomRepository.findByRoomAndUser(room, user);
        return userRoomInfo;
    }
}
