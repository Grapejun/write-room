package com.main.writeRoom.web.dto.note;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;

public class NoteRequestDTO {

    @Getter
    public static class createNoteDTO {
        String noteTitle;
        String noteSubtitle;
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
        String noteSubtitle;
        String noteContent;
        Boolean imgDelete;
        @NotNull(message = "카테고리 입력은 필수입니다.")
        Long categoryId;
        @NotNull(message = "글자수 입력은 필수입니다.")
        Long letterCount;
        ArrayList<String> noteTagList;
    }
}
