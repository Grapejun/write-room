package com.main.writeRoom.web.dto.category;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResult {
        Long roomId;
        Long allCountNote;
        List<CategoryList> categoryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryList {
        Long categoryId;
        Long countNote;
    }
}
