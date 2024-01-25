package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.service.CategoryService.CategoryCommandService;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO.RoomInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorys")
@Slf4j
public class CategoryController {
    private final CategoryCommandService categoryCommandService;

    @PostMapping("/create/{roomId}")
    public ApiResponse<RoomInfoResult> createCategory(@PathVariable(name = "roomId")Long roomId, @RequestBody
                                                      CategoryRequestDTO.CreateCategoryDTO request) {
        Room room = categoryCommandService.createCategory(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(room));
    }
}
