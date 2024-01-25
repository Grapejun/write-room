package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.service.CategoryService.CategoryCommandService;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO.RoomInfoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "사용자 카테고리 생성 API", description = "사용자 카테고리를 생성하는 API 이며, 카테고리 이름을 입력해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PostMapping("/create/{roomId}")
    public ApiResponse<RoomInfoResult> createCategory(@PathVariable(name = "roomId")Long roomId, @RequestBody
                                                      CategoryRequestDTO.CreateCategoryDTO request) {
        Room room = categoryCommandService.createCategory(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(room));
    }
}
