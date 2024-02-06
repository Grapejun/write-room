package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import java.util.List;

import com.main.writeRoom.domain.User.User;
import org.springframework.data.domain.Page;

public interface NoteQueryService {
    Page<Note> findNoteForRoomAndCategory(Category category, Room room, Integer page);
    List<Note> findNoteForCategoryAndRoom(Category category, Room room);
    Page<Note> getNoteListForRoom(Room room, Integer page);
    Note findNote(Long noteId);
    public Page<Note> findNoteListForRoomAndUser(Room room, User user, Integer page);
}
