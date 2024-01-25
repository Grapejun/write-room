package com.main.writeRoom.service.CategoryService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;

public interface CategoryCommandService {
    Room createCategory(Long roomId, CategoryRequestDTO.CreateCategoryDTO request);
}
