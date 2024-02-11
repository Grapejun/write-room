package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.BookmarkConverter;
import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.BookmarkHandler;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.BookmarkMaterialRepository;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.web.dto.bookmark.BookmarkRequestDTO;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkMaterialRepository bookmarkMaterialRepository;
    private final UserRepository userRepository;

    @Override
    public BookmarkMaterial postTopic(long userId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BookmarkMaterial newBookmarkMaterial = BookmarkMaterial.builder()
                .content(content)
                .user(user)
                .build();

        return bookmarkMaterialRepository.save(newBookmarkMaterial);
    }

    @Override
    public BookmarkResponseDTO.TopicResultDTO deleteMaterial(Long id) {

        BookmarkMaterial bookmarkMaterial = bookmarkMaterialRepository.findById(id)
                        .orElseThrow(() -> new BookmarkHandler(ErrorStatus.BOOKMARK_NOT_FOUND));

        bookmarkMaterialRepository.delete(bookmarkMaterial);

        return BookmarkConverter.toDeleteResultDTO(id);
    }
}
