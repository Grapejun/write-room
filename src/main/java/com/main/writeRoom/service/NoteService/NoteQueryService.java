package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import java.util.List;

public interface NoteQueryService {
    List<Note> findNoteForCategory(Category category);
}
