package com.main.writeRoom.web.dto.note;

import com.main.writeRoom.domain.ACHIEVE;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.mapping.NoteTag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteRequestDTO {

    @Getter
    public static class createNoteDTO {
        String noteTitle;
        String noteSubTitle;
        String noteCoverImage; // String 맞는지 확인
        String noteContent;
        Boolean challengeCheck;
        LocalDateTime createdAt;
        ArrayList<String> noteTagList; // 얘 스트링으로 받아
        Long userId;
        Long categoryId;
    }
}
