package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;
import org.springframework.data.domain.Page;

public interface NoteQueryService {
    List<Note> findNoteForCategoryAndRoom(Category category, Room room);
    Page<Note> getNoteListForRoom(Room room, Integer page);
    Note findNote(Long noteId);
}
