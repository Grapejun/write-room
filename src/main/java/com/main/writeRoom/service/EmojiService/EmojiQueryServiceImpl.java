package com.main.writeRoom.service.EmojiService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.handler.EmojiHandler;
import com.main.writeRoom.repository.EmojiClickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmojiQueryServiceImpl implements EmojiQueryService{
    private final EmojiClickRepository emojiClickRepository;
    public EmojiClick findByNoteAndUser(Note note, User user) {
        return emojiClickRepository.findByNoteAndUser(note, user).orElseThrow(() -> new EmojiHandler(ErrorStatus.EMOJI_NOT_FOUNT));
    }

    public List<EmojiClick> findAllByNote(Note note) {
        return emojiClickRepository.findAllByNote(note);
    }

}
