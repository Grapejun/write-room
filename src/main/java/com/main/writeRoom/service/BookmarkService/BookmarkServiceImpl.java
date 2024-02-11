package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.BookmarkConverter;
import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.BookmarkHandler;
import com.main.writeRoom.repository.BookmarkMaterialRepository;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkMaterialRepository bookmarkMaterialRepository;

    @Override
    public BookmarkMaterial postTopic(User user, String content) {

        if (content == null || content.isEmpty()) {
            throw new BookmarkHandler(ErrorStatus.CONTENT_MUST_NOT_BE_EMPTY);
        }

        BookmarkMaterial newBookmarkMaterial = BookmarkMaterial.builder()
                .content(content)
                .user(user)
                .build();

        return bookmarkMaterialRepository.save(newBookmarkMaterial);
    }

    @Override
    public BookmarkResponseDTO.TopicResultDTO deleteMaterial(BookmarkMaterial bookmarkMaterial) {

        bookmarkMaterialRepository.delete(bookmarkMaterial);

        return BookmarkConverter.toDeleteResultDTO(bookmarkMaterial);
    }
}
