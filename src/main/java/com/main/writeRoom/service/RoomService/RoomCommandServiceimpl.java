package com.main.writeRoom.service.RoomService;

import static com.main.writeRoom.domain.mapping.Authority.MANAGER;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.aws.s3.AmazonS3Manager;
import com.main.writeRoom.aws.s3.Uuid;
import com.main.writeRoom.converter.CategoryConverter;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.handler.RoomHandler;
import com.main.writeRoom.handler.RoomParticipationHandler;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.CategoryRepository;
import com.main.writeRoom.repository.RoomParticipationRepository;
import com.main.writeRoom.repository.RoomRepository;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.repository.UuidRepository;
import com.main.writeRoom.web.dto.room.RoomRequestDTO;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomCommandServiceimpl implements RoomCommandService {
    private final RoomParticipationRepository userRoomRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<RoomParticipation> getMyRoomResultList(Long userId, Integer page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("room.updatedAt")));
        return userRoomRepository.findAllByUser(user, pageRequest);
    }

    @Override
    public List<RoomParticipation> getMyRoomResultList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return userRoomRepository.findAllByUser(user);
    }


    @Override
    public Page<RoomParticipation> getUserRoomInfoList(Room room) {
        Page<RoomParticipation> userRoomInfoList = userRoomRepository.findAllByRoom(room, PageRequest.of(0, 10));
        return userRoomInfoList;
    }

    @Override
    public RoomParticipation getUserRoomInfo(Room room, User user) {
        RoomParticipation userRoomInfo = userRoomRepository.findByRoomAndUser(room, user);

        if (userRoomInfo == null) {
            throw new RoomHandler(ErrorStatus.ROOM_ALREADY_NOT_FOUND);
        }
        return userRoomInfo;
    }

    @Override
    @Transactional
    public Room createRoom(User user, RoomRequestDTO.CreateRoomDTO request, MultipartFile roomImg) {
        String imgUrl = null;
        Uuid savedUuid = null;
        if (roomImg != null && !roomImg.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid, "room"), roomImg);
        }

        Room room = RoomConverter.toRoom(request, imgUrl);
        RoomParticipation roomParticipation = RoomConverter.toUserRoom(room, user);
        userRoomRepository.save(roomParticipation);

        Room newRoom = roomRepository.save(room);
        Category category = CategoryConverter.toCategoryDefaultResult(newRoom);
        categoryRepository.save(category);

        return newRoom;
    }



    @Transactional
    public void deleteRoom(Room room, User user) {
        RoomParticipation roomParticipation = userRoomRepository.findByRoomAndUser(room, user);
        if (roomParticipation.getAuthority() != MANAGER) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }

        // 룸과 관련된 카테고리를 먼저 삭제합니다.
        categoryRepository.deleteAll(room.getCategoryList());

        roomRepository.delete(room);
    }

    public Page<RoomParticipation> findUpdateAtUserList(Room room, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return userRoomRepository.findAllByRoom(room, pageRequest);
    }

    @Transactional
    public Room roomParticipateIn(Room room, User user) {
        boolean isUserAlreadyParticipation = userRoomRepository.existsByRoomAndUser(room, user);
        if (isUserAlreadyParticipation) {
            throw new RoomParticipationHandler(ErrorStatus.ROOM_ALREADY_PARTICIPATION);
        }
        RoomParticipation response = RoomConverter.toUserParticipateIn(room, user);
        return userRoomRepository.save(response).getRoom();
    }

    public List<RoomParticipation> getMyRoomAllResultList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return userRoomRepository.findAllByUser(user);
    }

    @Transactional
    public Room updatedMyRoomInfo(Room room, RoomRequestDTO.UpdatedRoomInfoDTO request, MultipartFile roomImg, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        RoomParticipation roomParticipation = userRoomRepository.findByRoomAndUser(room, user);

        if (!Objects.equals(roomParticipation.getAuthority().toString(), "MANAGER")) {
            throw new RoomHandler(ErrorStatus.AUTHORITY_NOT_FOUND);
        }

        String imgUrl = null;
        Uuid savedUuid = null;
        if (roomImg != null && !roomImg.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid, "user"), roomImg);
        }

        return room.setRoomInfo(request.getRoomTitle(), request.getRoomIntroduction(), imgUrl);
    }
}
