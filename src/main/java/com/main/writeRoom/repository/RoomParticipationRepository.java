package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipationRepository extends JpaRepository<RoomParticipation, Long> {
    Page<RoomParticipation> findAllByUser(User user, PageRequest pageRequest);
    Page<RoomParticipation> findAllByRoom(Room room, PageRequest pageRequest);
    RoomParticipation findByRoomAndUser(Room room, User user);
    void deleteByRoomAndUser(Room room, User user);
    List<RoomParticipation> findAllByUser(User user);
    boolean existsByRoomAndUser(Room room, User user);
    List<RoomParticipation> findByRoom(Room room);
}
