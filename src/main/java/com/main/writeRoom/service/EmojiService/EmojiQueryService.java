package com.main.writeRoom.service.EmojiService;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;

import java.util.Optional;

public interface EmojiQueryService {
    EmojiClick findByNoteAndUser(Note note, User user);
}
