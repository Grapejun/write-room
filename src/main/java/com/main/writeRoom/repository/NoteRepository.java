package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByCategoryAndRoom(Category category, Room room);
}
