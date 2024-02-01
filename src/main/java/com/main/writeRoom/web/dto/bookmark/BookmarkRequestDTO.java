package com.main.writeRoom.web.dto.bookmark;

import com.main.writeRoom.domain.User.User;
import lombok.Getter;

public class BookmarkRequestDTO {

    @Getter
    public static class TopicDTO {
        String content;
        Long userId;
    }
}
