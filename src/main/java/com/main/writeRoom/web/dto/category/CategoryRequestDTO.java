package com.main.writeRoom.web.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CategoryRequestDTO {

    @Getter
    public static class CreateCategoryDTO {
        @NotBlank
        String categoryName;
    }
}
