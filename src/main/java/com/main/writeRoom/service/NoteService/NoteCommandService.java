package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Note;

public interface NoteCommandService {
    void createBookmarkNote(Long roomId, Note note, Long userId);
    void deleteBookmarkNote(Long bookmarkNoteId);
}
