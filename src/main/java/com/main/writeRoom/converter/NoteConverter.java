package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class NoteConverter {

public static NoteResponseDTO.RoomResult toRoomResultDTO(Room room, Page<Note> notes) {
    List<NoteResponseDTO.NoteList> toRoomResultNoteDTOList = notes.stream()
            .map(NoteConverter::toRoomResultNoteDTOList).collect(Collectors.toList());

    return NoteResponseDTO.RoomResult.builder()
            .roomId(room.getId())
            .noteList(toRoomResultNoteDTOList)
            .build();
}


    public static NoteResponseDTO.NoteList toRoomResultNoteDTOList(Note note) {
    List<TagResponseDTO.TagList> toNoteResultTagDTOList = note.getNoteTagList().stream()
            .map(NoteConverter::toNoteResultTagDTOList).collect(Collectors.toList());

        return NoteResponseDTO.NoteList.builder()
                .noteId(note.getId())
                .noteTitle(note.getTitle())
                .noteSubtitle(note.getSubtitle())
                .noteContent(note.getContent())
                .noteImg(note.getCoverImg())
                .writer(note.getUser().getName())
                .userProfileImg(note.getUser().getProfileImage())
                .createdAt(note.getCreatedAt())
                .tagList(toNoteResultTagDTOList)
                .build();
    }

    public static TagResponseDTO.TagList toNoteResultTagDTOList(NoteTag noteTag) {
        return TagResponseDTO.TagList.builder()
                .tagId(noteTag.getTag().getId())
                .tagName(noteTag.getTag().getContent())
                .build();
    }
}
