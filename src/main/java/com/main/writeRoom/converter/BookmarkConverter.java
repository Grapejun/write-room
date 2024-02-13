package com.main.writeRoom.converter;

import static java.util.stream.Collectors.toList;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import com.main.writeRoom.web.dto.tag.TagResponseDTO.TagList;
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
                .collect(toList());

        return BookmarkResponseDTO.BookMarkMaterialListDTO.builder()
                .isLast(bookmarkMaterialPage.isLast())
                .isFirst(bookmarkMaterialPage.isFirst())
                .totalPage(bookmarkMaterialPage.getTotalPages())
                .totalElements(bookmarkMaterialPage.getTotalElements())
                .listSize(bookmarkMaterialDTOList.size())
                .bookmarkMaterialList(bookmarkMaterialDTOList) // DTO 리스트 사용
                .build();
    }

    public static BookmarkResponseDTO.NoteListForNoteBookmark toNoteBookmarkListResult(Page<BookmarkNote> bookmarkNotePage) {
        List<BookmarkResponseDTO.NoteListForNoteBookmarkList> bookmarkLists = bookmarkNotePage.getContent()
                .stream()
                .map(bookmarkNote -> {
                    List<TagList> tagList = bookmarkNote.getNote().getNoteTagList()
                            .stream()
                            .map(NoteTag::getTag)
                            .map(tag -> new TagList(tag.getId(), tag.getContent()))
                            .toList();

                    return BookmarkResponseDTO.NoteListForNoteBookmarkList.builder()
                            .roomId(bookmarkNote.getRoom().getId())
                            .noteId(bookmarkNote.getNote().getId())
                            .noteTitle(bookmarkNote.getNote().getTitle())
                            .noteSubtitle(bookmarkNote.getNote().getSubtitle())
                            .noteContent(bookmarkNote.getNote().getContent())
                            .noteImg(bookmarkNote.getNote().getCoverImg())
                            .writer(bookmarkNote.getNote().getUser().getName())
                            .writerImg(bookmarkNote.getNote().getUser().getProfileImage())
                            .createdAt(bookmarkNote.getNote().getCreatedAt())
                            .tagList(tagList)
                            .build();
                })
                .collect(Collectors.toList());

        return BookmarkResponseDTO.NoteListForNoteBookmark.builder()
                .isLast(bookmarkNotePage.isLast())
                .isFirst(bookmarkNotePage.isFirst())
                .totalPage(bookmarkNotePage.getTotalPages())
                .totalElements(bookmarkNotePage.getTotalElements())
                .listSize(bookmarkLists.size())
                .noteListForNoteBookmarkLists(bookmarkLists)
                .build();
    }
}
