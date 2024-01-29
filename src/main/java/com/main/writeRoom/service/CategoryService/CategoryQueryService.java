package com.main.writeRoom.service.CategoryService;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import java.util.List;

public interface CategoryQueryService {
    Category findCategory(Long categoryId);
    List<Category> findCategoryForRoom(Room room);
}
