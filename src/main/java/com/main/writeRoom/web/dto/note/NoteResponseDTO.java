package com.main.writeRoom.web.dto.note;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.emoji.EmojiResponseDTO;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

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
        String roomImg;
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
        Long categoryId;
        String categoryContent;
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
        String noteSubtitle;
        String noteContent;
        String categoryName;
        String writer;
        String noteCoverImg;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        List<TagResponseDTO.TagList> tagList;
        EmojiResponseDTO.EmojiListResult emojiList;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteDeleteResult {
        Long noteId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteListDTO {
        private List<SearchNoteDTO> noteList;
        private List<String> writerList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteListWithMemberFilterDTO {
        private List<SearchNoteDTO> noteList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchNoteDTO {
        private String roomName;
        private Long noteId;
        private String writer;
        private String profileImg;
        LocalDateTime  createdAt;

        private String title;
        private String subtitle;
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor

    public static class RoomResultForTag {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Long roomId;
        String roomTitle;
        String roomIntroduction;
        List<NoteListForTag> noteListForTags;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteListForTag {
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
}
