package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
}
