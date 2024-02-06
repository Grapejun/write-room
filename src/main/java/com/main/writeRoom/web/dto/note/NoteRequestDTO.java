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
        String noteContent;
        Long letterCount;
        ArrayList<String> noteTagList; // 얘 스트링으로 받아
        Long userId;
        Long categoryId;
    }

    // 노트 수정이 가능한 컨텐츠들 집어 넣기
    @Getter
    public static class patchNoteDTO {
        String noteTitle;
        String noteSubtitle;
        String noteContent;
        Long categoryId;
        Long letterCount;
        ArrayList<String> noteTagList;
        //userId 만 빠졌다
    }
}
