package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.repository.BookmarkNoteRepository;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteCommandServiceImpl implements NoteCommandService{

    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final BookmarkNoteRepository bookmarkNoteRepository;

    @Transactional
    public void createBookmarkNote(Long roomId, Note note, Long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        BookmarkNote bookmarkNote = NoteConverter.toBookMarkNote(room, note, user);
        bookmarkNoteRepository.save(bookmarkNote);
    }

    @Transactional
    public void deleteBookmarkNote(Long bookmarkNoteId) {
        bookmarkNoteRepository.deleteById(bookmarkNoteId);
    }
}
