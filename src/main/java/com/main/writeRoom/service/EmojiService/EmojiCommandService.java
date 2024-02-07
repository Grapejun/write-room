package com.main.writeRoom.service.EmojiService;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.web.dto.emoji.EmojiResponseDTO;

public interface EmojiCommandService {
    EmojiResponseDTO.EmojiClickResult postEmoji(Note note, User user, Long emojiNum);
    EmojiResponseDTO.EmojiUpdateResult updateEmoji(Note note, User user, EmojiClick emojiClick, Long emojiNum);

    EmojiResponseDTO.EmojiDeleteResult deleteEmoji(EmojiClick emojiClick);

}
