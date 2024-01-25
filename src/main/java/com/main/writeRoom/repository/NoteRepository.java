package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    //Note findByUserAndRoom(User user, Room room);
    @Query("select n from Note n where n.createdAt>= :startDate and n.createdAt<= :deadline and n.user = :user and n.room = :room")
    List<Note> findNotesByDate(@Param("startDate") LocalDateTime startDate, @Param("deadline") LocalDateTime deadline, @Param("user") User user, @Param("room") Room room);

}
