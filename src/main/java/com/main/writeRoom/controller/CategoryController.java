package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.service.CategoryService.CategoryCommandService;
import com.main.writeRoom.service.CategoryService.CategoryQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.web.dto.category.CategoryRequestDTO;
import com.main.writeRoom.web.dto.category.CategoryResponseDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO.RoomInfoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final CategoryQueryService categoryQueryService;
    private final RoomQueryService roomQueryService;

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

    @Operation(summary = "사용자 카테고리 삭제 API", description = "사용자 카테고리를 삭제하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4001", description = "카테고리가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),

    })
    @DeleteMapping("/{roomId}/{categoryId}")
    public ApiResponse<RoomInfoResult> deleteCategory(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "categoryId")Long categoryId) {
        Room room = categoryCommandService.deleteCategory(roomId, categoryId);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(room));
    }

    @Operation(summary = "카테고리 이름 수정 API", description = "카테고리 이름을 수정하는 API이며, 수정 할 이름을 입력해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4001", description = "카테고리가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping("/updated/{categoryId}")
    public ApiResponse<RoomResponseDTO.RoomInfoResult> updateCategory(@PathVariable(name = "categoryId")Long categoryId,
                                                                      @RequestBody CategoryRequestDTO.CreateCategoryDTO request) {
        Category category = categoryCommandService.updatedCategory(categoryId, request.getCategoryName());
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(category.getRoom()));
    }

    @Operation(summary = "전체 노트 카운트, 카테고리에 속해있는 노트 카운트 조회 API", description = "전체 노트 수와 룸에 속해있는 카테고리가 지정된 노트 카운트를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
    })
    @GetMapping("/category/{roomId}")
    public ApiResponse<CategoryResponseDTO.CategoryResult> getCategoryList(@PathVariable(name = "roomId")Long roomId) {
        Room room = roomQueryService.findRoom(roomId);
        CategoryResponseDTO.CategoryResult response = categoryCommandService.findCategoryForRoom(room);
        return ApiResponse.of(SuccessStatus._OK, response);
    }
}