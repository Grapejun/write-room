package com.main.writeRoom.repository;

import com.main.writeRoom.domain.mapping.EmojiClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiClickRepository extends JpaRepository<EmojiClick, Long> {
}
