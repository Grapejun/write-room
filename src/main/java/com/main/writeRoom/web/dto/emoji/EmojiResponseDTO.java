package com.main.writeRoom.web.dto.emoji;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EmojiResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiClickResult {
        Long emojiClickId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiDeleteResult {
        Long emojiClickId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiUpdateResult {
        Long emojiId;
        Long emojiNum;
        LocalDateTime updatedAT;
    }

    // 개별 이모지 조회

    // 이모지 리스트 조회
}
