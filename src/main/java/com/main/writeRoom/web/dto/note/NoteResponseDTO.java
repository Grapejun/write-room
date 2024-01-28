package com.main.writeRoom.web.dto.note;

import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoteResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomResult {
        Long roomId;
        String roomTitle;
        String roomIntroduction;
        List<NoteList> noteList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteList {
        Long noteId;
        String noteTitle;
        String noteSubtitle;
        String noteContent;
        String writer;
        String noteImg;
        String userProfileImg;
        LocalDateTime createdAt;
        List<TagResponseDTO.TagList> tagList;
    }
}
