package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import org.springframework.data.domain.Page;

public interface BookmarkService {
    BookmarkMaterial postTopic(User user, String content);
    BookmarkResponseDTO.TopicResultDTO deleteMaterial(Long userId, BookmarkMaterial bookmarkMaterial);
    Page<BookmarkNote> findNoteBookmark(User user, Integer page);
    }
