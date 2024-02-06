package com.main.writeRoom.service.CategoryService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.handler.CategoryHandler;
import com.main.writeRoom.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long categoryId) {
        System.out.println("Requested categoryId: " + categoryId); // 로그 추가
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryHandler(ErrorStatus.CATEGORY_NOT_FOUND));
    }

    public List<Category> findCategoryForRoom(Room room) {
        return categoryRepository.findAllByRoom(room);
    }
}
