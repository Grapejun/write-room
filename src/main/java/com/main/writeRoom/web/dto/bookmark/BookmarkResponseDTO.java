package com.main.writeRoom.web.dto.bookmark;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
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
        Long BookmarkId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicSearchResultDTO {
        Long userId;
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
        List<BookmarkMaterialDTO> bookmarkMaterialList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
}
