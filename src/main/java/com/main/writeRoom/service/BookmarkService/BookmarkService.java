package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;

public interface BookmarkService {
    BookmarkMaterial postTopic(User user, String content);
    BookmarkResponseDTO.TopicResultDTO deleteMaterial(BookmarkMaterial bookmarkMaterial);
    }
