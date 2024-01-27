package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.main.writeRoom.domain.User.User;

import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByCategoryAndRoom(Category category, Room room);

    //Note findByUserAndRoom(User user, Room room);
    @Query("select n from Note n where n.createdAt>= :startDate and n.createdAt<= :deadline and n.user = :user and n.room = :room and n.achieve = 'TRUE'")
    List<Note> findNotes(@Param("startDate") LocalDateTime startDate, @Param("deadline") LocalDateTime deadline, @Param("user") User user, @Param("room") Room room);
}
