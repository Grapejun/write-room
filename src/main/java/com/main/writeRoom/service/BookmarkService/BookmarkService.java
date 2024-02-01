package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.web.dto.bookmark.BookmarkRequestDTO;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;

public interface BookmarkService {

    public BookmarkMaterial postTopic(BookmarkRequestDTO.TopicDTO request);

    public BookmarkResponseDTO.TopicResultDTO deleteMaterial(Long id);
    }
