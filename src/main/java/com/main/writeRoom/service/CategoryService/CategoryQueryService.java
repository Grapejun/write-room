package com.main.writeRoom.service.CategoryService;

import com.main.writeRoom.domain.Category;

public interface CategoryQueryService {
    Category findCategory(Long categoryId);
}
