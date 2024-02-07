package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmojiClickRepository extends JpaRepository<EmojiClick, Long> {
    Optional<EmojiClick> findByNoteAndUser(Note note, User user);

}
