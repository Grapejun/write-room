package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.mapping.NoteTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {
    Page<NoteTag> findAllByRoom(Room room, PageRequest pageRequest);
    Page<NoteTag> findAllByRoomAndTag(Room room, Tag tag, PageRequest pageRequest);
}
