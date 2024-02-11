package com.main.writeRoom.web.dto.note;

import com.main.writeRoom.domain.ACHIEVE;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.mapping.NoteTag;
import jakarta.validation.constraints.NotNull;
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
        @NotNull(message = "카테고리 입력은 필수입니다.")
        Long categoryId;
        @NotNull(message = "글자수 입력은 필수입니다.")
        Long letterCount;
        ArrayList<String> noteTagList;
    }

    @Getter
    public static class patchNoteDTO {
        String noteTitle;
        String noteSubTitle;
        String noteContent;
        @NotNull(message = "카테고리 입력은 필수입니다.")
        Long categoryId;
        @NotNull(message = "글자수 입력은 필수입니다.")
        Long letterCount;
        ArrayList<String> noteTagList;
    }
}
