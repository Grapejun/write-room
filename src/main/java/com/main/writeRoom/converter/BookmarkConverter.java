package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookmarkConverter {

    public static BookmarkResponseDTO.TopicResultDTO toBookmarkResultDTO(BookmarkMaterial topic) {
        return BookmarkResponseDTO.TopicResultDTO.builder()
                .bookmarkId(topic.getId())
                .bookmarkContent(topic.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookmarkResponseDTO.TopicResultDTO toDeleteResultDTO(BookmarkMaterial bookmarkMaterial) {
        return BookmarkResponseDTO.TopicResultDTO.builder()
                .bookmarkId(bookmarkMaterial.getId())
                .bookmarkContent(bookmarkMaterial.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookmarkResponseDTO.BookMarkMaterialListDTO toBookMarkMaterialListDTO(Page<BookmarkMaterial> bookmarkMaterialPage) {
        List<BookmarkResponseDTO.BookmarkMaterialDTO> bookmarkMaterialDTOList = bookmarkMaterialPage.getContent()
                .stream()
                .map(bookmarkMaterial -> BookmarkResponseDTO.BookmarkMaterialDTO.builder()
                        .id(bookmarkMaterial.getId())
                        .content(bookmarkMaterial.getContent())
                        .build())
                .collect(Collectors.toList());

        return BookmarkResponseDTO.BookMarkMaterialListDTO.builder()
                .isLast(bookmarkMaterialPage.isLast())
                .isFirst(bookmarkMaterialPage.isFirst())
                .totalPage(bookmarkMaterialPage.getTotalPages())
                .totalElements(bookmarkMaterialPage.getTotalElements())
                .listSize(bookmarkMaterialDTOList.size())
                .bookmarkMaterialList(bookmarkMaterialDTOList) // DTO 리스트 사용
                .build();
    }

}
