package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "EmojiClick")
public class EmojiClick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emoji")
    private Emoji emoji;

    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;
}
