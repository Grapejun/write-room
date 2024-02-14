package com.main.writeRoom.web.dto.search;

import lombok.*;

public class SearchResponseDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VocabularyResultDTO {
        String voca;
    }
}
