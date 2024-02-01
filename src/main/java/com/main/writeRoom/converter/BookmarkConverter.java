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
                .BookmarkId(topic.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookmarkResponseDTO.TopicResultDTO toDeleteResultDTO(Long id) {
        return BookmarkResponseDTO.TopicResultDTO.builder()
                .BookmarkId(id)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BookmarkResponseDTO.BookMarkMaterialListDTO toBookMarkMaterialListDTO(Page<BookmarkMaterial> bookmarkMaterialPage) {
        List<BookmarkResponseDTO.BookmarkMaterialDTO> bookmarkMaterialDTOList = bookmarkMaterialPage.getContent()
                .stream()
                .map(bookmarkMaterial -> BookmarkResponseDTO.BookmarkMaterialDTO.builder()
                        .id(bookmarkMaterial.getId())
                        .content(bookmarkMaterial.getContent())
                        // user 정보는 복사하지 않음
                        .build())
                .collect(Collectors.toList());

        return BookmarkResponseDTO.BookMarkMaterialListDTO.builder()
                .isLast(bookmarkMaterialPage.isLast())
                .isFirst(bookmarkMaterialPage.isFirst())
                .totalPage(bookmarkMaterialPage.getTotalPages())
                .listSize(bookmarkMaterialDTOList.size())
                .bookmarkMaterialList(bookmarkMaterialDTOList) // DTO 리스트 사용
                .build();
    }

}
