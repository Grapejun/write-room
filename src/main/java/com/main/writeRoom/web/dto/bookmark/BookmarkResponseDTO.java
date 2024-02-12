package com.main.writeRoom.web.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BookmarkResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicResultDTO {
        Long bookmarkId;
        String bookmarkContent;
        LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkMaterialDTO {
        private Long id;
        private String content;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookMarkMaterialListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        List<BookmarkMaterialDTO> bookmarkMaterialList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteListForNoteBookmark {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        List<NoteListForNoteBookmarkList> noteListForNoteBookmarkLists;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteListForNoteBookmarkList {
        Long noteId;
        String noteTitle;
        String noteContent;
        String writer;
        LocalDateTime createdAt;
    }
}
