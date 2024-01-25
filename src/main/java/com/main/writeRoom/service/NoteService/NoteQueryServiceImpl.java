package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.repository.NoteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
}
