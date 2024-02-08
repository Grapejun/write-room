package com.main.writeRoom.service.TagService;

import com.main.writeRoom.domain.mapping.NoteTag;
import org.springframework.data.domain.Page;

public interface TagQueryService {
    Page<NoteTag> getTagListForRoom(Long roomId, Integer page);
}
