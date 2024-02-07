package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EmojiClick")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

}
