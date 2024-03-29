package com.main.writeRoom.web.dto.emoji;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
        Long emojiId;
        Long userId;
        Long emojiNum;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiUpdateResult {
        Long emojiId;
        Long emojiNum;
        LocalDateTime updatedAt;
    }

    // 개별 이모지 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiGetResult {
        Long noteId;
        Long userId;
        Long emojiId;
        Long emojiNum;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    // 이모지 리스트 조회
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiListResult {
        List<Integer> emojiCounts; // 이모지 카운트를 담는 리스트

    }
}
