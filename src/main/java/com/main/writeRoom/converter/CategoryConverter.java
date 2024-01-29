package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;
import com.main.writeRoom.web.dto.category.CategoryResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter {

    public static Category toCategory(CategoryRequestDTO.CreateCategoryDTO request, Room room) {
        return Category.builder()
                .name(request.getCategoryName())
                .room(room)
                .build();
    }

    public static CategoryResponseDTO.CategoryResult toCategoryForRoom(Room room, Long noteAllCount, List<Category> categoryList) {
        List<CategoryResponseDTO.CategoryList> toCategoryForRoomList = categoryList.stream()
                .map(category -> CategoryInfoList(category,room.getNoteList()))
                .collect(Collectors.toList());

        return CategoryResponseDTO.CategoryResult.builder()
                .roomId(room.getId())
                .allCountNote(noteAllCount)
                .categoryList(toCategoryForRoomList)
                .build();
    }

    public static CategoryResponseDTO.CategoryList CategoryInfoList(Category category, List<Note> noteList) {
        long countNote = noteList.stream()
                .filter(note -> note.getCategory().equals(category))
                .count();

        return CategoryResponseDTO.CategoryList.builder()
                .categoryId(category.getId())
                .countNote(countNote)
                .build();
    }
}