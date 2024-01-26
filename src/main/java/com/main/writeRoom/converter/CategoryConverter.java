package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;

public class CategoryConverter {
    public static Category toCategory(CategoryRequestDTO.CreateCategoryDTO request, Room room) {
        return Category.builder()
                .name(request.getCategoryName())
                .room(room)
                .build();
    }
}