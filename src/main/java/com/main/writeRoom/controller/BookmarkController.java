package com.main.writeRoom.controller;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.converter.BookmarkConverter;
import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.service.BookmarkService.BookmarkQueryService;
import com.main.writeRoom.service.BookmarkService.BookmarkServiceImpl;
import com.main.writeRoom.web.dto.bookmark.BookmarkRequestDTO;
import com.main.writeRoom.web.dto.bookmark.BookmarkResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkServiceImpl bookmarkService;
    private final BookmarkQueryService bookmarkQueryService;

    @PostMapping("/topics")
    @Operation(summary = "오늘의 소재 북마크 등록 API", description = "사용자가 특정 '오늘의 소재'를 북마크에 추가하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    public ApiResponse<BookmarkResponseDTO.TopicResultDTO> postBookmark(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "사용자의 id와 북마크할 내용을 등록해야 합니다.", required = true, content = @Content(schema = @Schema(implementation = BookmarkRequestDTO.TopicDTO.class)))
            @RequestBody @Valid BookmarkRequestDTO.TopicDTO request) {
        BookmarkMaterial material = bookmarkService.postTopic(request);
        return ApiResponse.onSuccess(BookmarkConverter.toBookmarkResultDTO(material));
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "오늘의 소재 북마크 삭제 API", description = "특정 사용자가 북마크에 등록한 '오늘의 소재'를 해제하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.",content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "id", description = "북마크 아이디, path variable 입니다!"),
    })
    public ApiResponse<BookmarkResponseDTO.TopicResultDTO> deleteBookmark(@PathVariable Long id) {
        return ApiResponse.onSuccess(bookmarkService.deleteMaterial(id));
    }

    @GetMapping("/topics/{userId}")
    @Operation(summary = "북마크한 오늘의 소재 목록 조회 API", description = "특정 사용자가 북마크한 '오늘의 소재' 목록을 조회하는 API 이며 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "사용자 아이디, path variable 입니다!"),
            @Parameter(name = "page", description = "페이지 번호, 0번이 1페이지 입니다.")
    })
    public ApiResponse<BookmarkResponseDTO.BookMarkMaterialListDTO> getBookmarks(@PathVariable Long userId, @RequestParam Integer page) {
        return ApiResponse.onSuccess(BookmarkConverter.toBookMarkMaterialListDTO(bookmarkQueryService.getBookmarkMaterialList(userId, page)));
    }

}
