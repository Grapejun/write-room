package com.main.writeRoom.service.BookmarkService;

import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import org.springframework.data.domain.Page;

public interface BookmarkQueryService {

    Page<BookmarkMaterial> getBookmarkMaterialList(Long userId, Integer page);
}
