package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.web.dto.emoji.EmojiResponseDTO;

import java.time.LocalDateTime;

public class EmojiConverter {

    public static Emoji toEmoji(User user, Long emojiNum) {

        return Emoji.builder()
                .user(user)
                .emojiNum(emojiNum)
                .build();
    }

    public static EmojiResponseDTO.EmojiClickResult toEmojiClickResult(EmojiClick emojiClick) {

        return EmojiResponseDTO.EmojiClickResult.builder()
                .emojiClickId(emojiClick.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static EmojiResponseDTO.EmojiUpdateResult toEmojiUpdateResult(Emoji emoji) {
        return EmojiResponseDTO.EmojiUpdateResult.builder()
                .emojiId(emoji.getId())
                .emojiNum(emoji.getEmojiNum())
                .updatedAt(emoji.getUpdatedAt())
                .build();
    }

    public static EmojiResponseDTO.EmojiDeleteResult toEmojiDeleteResult(Emoji emoji) {

        return EmojiResponseDTO.EmojiDeleteResult.builder()
                .emojiId(emoji.getId())
                .userId(emoji.getUser().getId())
                .emojiNum(emoji.getEmojiNum())
                .build();
    }
    public static EmojiResponseDTO.EmojiGetResult toEmojiGetResult(EmojiClick emojiClick) {

        Emoji emoji = emojiClick.getEmoji();
        return EmojiResponseDTO.EmojiGetResult.builder()
                .noteId(emojiClick.getNote().getId())
                .userId(emojiClick.getUser().getId())
                .emojiId(emoji.getId())
                .emojiNum(emoji.getEmojiNum())
                .createdAt(emoji.getCreatedAt())
                .updatedAt(emoji.getUpdatedAt())
                .build();
    }

}
