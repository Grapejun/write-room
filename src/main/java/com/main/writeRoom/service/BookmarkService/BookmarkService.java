package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.web.dto.bookmark.BookmarkRequestDTO;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;

public interface BookmarkService {

    BookmarkMaterial postTopic(long userId, String content);
    BookmarkResponseDTO.TopicResultDTO deleteMaterial(Long id);
    }
