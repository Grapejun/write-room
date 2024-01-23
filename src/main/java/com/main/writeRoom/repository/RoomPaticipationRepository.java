package com.main.writeRoom.repository;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPaticipationRepository extends JpaRepository<RoomParticipation, Long> {
    Page<RoomParticipation> findAllByUser(User user, PageRequest pageRequest);
}
