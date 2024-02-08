package com.main.writeRoom.converter;

import com.main.writeRoom.domain.*;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class NoteConverter {

public static NoteResponseDTO.RoomResult toRoomResultDTO(Room room, Page<Note> notes) {
    List<NoteResponseDTO.NoteList> toRoomResultNoteDTOList = notes.stream()
            .map(NoteConverter::toRoomResultNoteDTOList).collect(Collectors.toList());

    return NoteResponseDTO.RoomResult.builder()
            .isFirst(notes.isFirst())
            .isLast(notes.isFirst())
            .totalPage(notes.getTotalPages())
            .totalElements(notes.getTotalElements())
            .listSize(notes.getSize())
            .roomId(room.getId())
            .roomTitle(room.getTitle())
            .roomIntroduction(room.getIntroduction())
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

    public static NoteResponseDTO.NoteResult toNoteResponseDTO(Note note) {

        return NoteResponseDTO.NoteResult.builder()
                .noteCoverImg(note.getCoverImg())
                .noteSubTitle(note.getSubtitle())
                .noteTitle(note.getTitle())
                .noteContent(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .noteId(note.getId())
                .categoryName(note.getCategory().getName())
                .emojiList(new ArrayList<>())
                .writer(note.getUser().getName())
                .tagList(new ArrayList<>())
                .build();
    }

    public static TagResponseDTO.TagList toNoteResultTagDTOList(NoteTag noteTag) {
        return TagResponseDTO.TagList.builder()
                .tagId(noteTag.getTag().getId())
                .tagName(noteTag.getTag().getContent())
                .build();
    }

    public static NoteResponseDTO.NoteResult toBookMarkNoteResult(Note note) {
        return NoteResponseDTO.NoteResult.builder()
                .noteId(note.getId())
                .build();
    }

    public static NoteResponseDTO.NoteDeleteResult toDeleteNoteResult(Note note) {
    return NoteResponseDTO.NoteDeleteResult.builder()
            .noteId(note.getId())
            .build();
    }

    // public static NoteResponseDTO.NoteResult toNoteDelete
    /*
    public static BookmarkResponseDTO.TopicResultDTO toDeleteResultDTO(Long id) {
        return BookmarkResponseDTO.TopicResultDTO.builder()
                .BookmarkId(id)
                .createdAt(LocalDateTime.now())
                .build();
    }
    */

    public static Note toNote(Room room, User user, Category category, NoteRequestDTO.createNoteDTO request, String imgUrl) {

    // challengeCheck
        ACHIEVE achieve = ACHIEVE.FALSE;
        // 200자 이상 시 true 로직 구현 -> check 받으면 TRUE로 받기
        if (request.getLetterCount() >= 200)
            achieve = ACHIEVE.TRUE;

        return Note.builder()
                .title(request.getNoteTitle())
                .subtitle(request.getNoteSubTitle())
                .coverImg(imgUrl)
                .content(request.getNoteContent())
                .achieve(achieve)
                .noteTagList(new ArrayList<>())
                .room(room)
                .category(category)
                .user(user)
                .build();
    }


    public static BookmarkNote toBookMarkNote(Room room, Note note, User user) {
        return BookmarkNote.builder()
                .room(room)
                .note(note)
                .user(user)
                .build();
    }
}
