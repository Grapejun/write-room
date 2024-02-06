package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.NoteHandler;
import com.main.writeRoom.repository.NoteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteQueryServiceImpl implements NoteQueryService{
    private final NoteRepository noteRepository;

    public List<Note> findNoteForCategoryAndRoom(Category category, Room room) {
        return noteRepository.findAllByCategoryAndRoom(category, room);
    }

    public Page<Note> getNoteListForRoom(Room room, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoom(room, pageRequest);
    }

    public Note findNote(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteHandler(ErrorStatus.NOTE_NOT_FOUND));
    }
    public Page<Note> findNoteForRoomAndCategory(Category category, Room room, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoomAndCategory(room, category, pageRequest);
    }

    public Page<Note> findNoteListForRoomAndUser(Room room, User user, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoomAndUser(room, user, pageRequest);
    }
}
