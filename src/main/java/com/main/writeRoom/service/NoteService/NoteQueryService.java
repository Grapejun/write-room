package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;

public interface NoteQueryService {
    List<Note> findNoteForCategoryAndRoom(Category category, Room room);
}
