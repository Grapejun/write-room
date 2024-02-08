package com.main.writeRoom.web.dto.note;

import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoteResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomResult {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Long roomId;
        String roomTitle;
        String roomIntroduction;
        List<NoteList> noteList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteList {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

        Long noteId;
        String noteTitle;
        String noteSubtitle;
        String noteContent;
        String writer;
        String noteImg;
        String userProfileImg;
        LocalDateTime createdAt;
        List<TagResponseDTO.TagList> tagList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreNoteResult {
        Note note;
        List<Tag> tagList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteResult {
        Long noteId;
        String noteTitle;
        String noteSubTitle;
        String noteContent;
        String categoryName;
        String writer;
        String noteCoverImg;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        List<TagResponseDTO.TagList> tagList;
        List<Emoji> emojiList; // EmojiResponseDTO로 변경할지 고민

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteDeleteResult {
        Long noteId;
    }

}
