package com.main.writeRoom.service.EmojiService;

import com.main.writeRoom.converter.EmojiConverter;
import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.repository.EmojiClickRepository;
import com.main.writeRoom.repository.EmojiRepository;
import com.main.writeRoom.web.dto.emoji.EmojiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmojiCommandServiceImpl implements EmojiCommandService{

    private final EmojiRepository emojiRepository;
    private final EmojiClickRepository emojiClickRepository;

    @Transactional
    public EmojiResponseDTO.EmojiClickResult postEmoji(Note note, User user, Long emojiNum) {

        Emoji emoji = EmojiConverter.toEmoji(user, emojiNum);
        emojiRepository.save(emoji);

        EmojiClick emojiClick = EmojiClick.builder()
                .emoji(emoji)
                .user(user)
                .note(note)
                .build();
        emojiClickRepository.save(emojiClick);

        return EmojiConverter.toEmojiClickResult(emojiClick);
    }
}
