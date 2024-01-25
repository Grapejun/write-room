package com.main.writeRoom.service.CategoryService;

import com.main.writeRoom.converter.CategoryConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.repository.CategoryRepository;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryCommandServiceImpl implements CategoryCommandService{
    private final RoomQueryService roomQueryService;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Room createCategory(Long roomId, CategoryRequestDTO.CreateCategoryDTO request) {
        Room room = roomQueryService.findRoom(roomId);

        Category category = CategoryConverter.toCategory(request, room);
        categoryRepository.save(category);
        return room;
    }
}
