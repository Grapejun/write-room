package com.main.writeRoom.repository;

import com.main.writeRoom.domain.mapping.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {
}
