package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface NoteCommandService {
    public NoteResponseDTO.PreNoteResult createPreNote(Room room, User user, Category category, MultipartFile noteImg, NoteRequestDTO.createNoteDTO request);
    public Note createNote(NoteResponseDTO.PreNoteResult preNoteResult);
    public Note updateNoteFields(Note existingNote, Category category, MultipartFile noteImg, NoteRequestDTO.patchNoteDTO request);

    void createBookmarkNote(Long roomId, Note note, Long userId);
    void deleteBookmarkNote(Long bookmarkNoteId);
    NoteResponseDTO.NoteDeleteResult deleteNote(Long noteId, User user);
}
