package com.main.writeRoom.controller;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.BookmarkConverter;
import com.main.writeRoom.domain.Bookmark.BookmarkMaterial;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.BookmarkHandler;
import com.main.writeRoom.service.BookmarkService.BookmarkQueryService;
import com.main.writeRoom.service.BookmarkService.BookmarkServiceImpl;
import com.main.writeRoom.service.UserService.UserQueryService;
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
    private final UserQueryService userQueryService;


    @PostMapping("/topics")
    @Operation(summary = "오늘의 소재 북마크 등록 API", description = "특정한 '오늘의 소재'를 사용자의 북마크에 추가하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "user", description = "사용자", hidden = true),
            @Parameter(name = "content", description = "북마크 내용")
    })
    public ApiResponse<BookmarkResponseDTO.TopicResultDTO> postBookmark(@AuthUser long userId, @RequestParam String content) {
        User user = userQueryService.findUser(userId);

        BookmarkMaterial material = bookmarkService.postTopic(user, content);
        return ApiResponse.of(SuccessStatus._OK, BookmarkConverter.toBookmarkResultDTO(material));
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "오늘의 소재 북마크 삭제 API", description = "특정 사용자가 북마크에 등록한 '오늘의 소재'를 삭제하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK4001", description = "해당하는 Id의 북마크가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK4002", description = "해당 사용자가 등록한 북마크가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "사용자", hidden = true),
            @Parameter(name = "id", description = "북마크 아이디, path variable 입니다!"),
    })
    public ApiResponse<BookmarkResponseDTO.TopicResultDTO> deleteBookmark(@AuthUser long userId, @PathVariable Long id) {
        BookmarkMaterial bookmarkMaterial = bookmarkQueryService.findBookmarkMaterial(id);
        return ApiResponse.of(SuccessStatus._OK, bookmarkService.deleteMaterial(userId, bookmarkMaterial));
    }

    @GetMapping("/topics")
    @Operation(summary = "북마크한 오늘의 소재 목록 조회 API", description = "특정 사용자가 북마크한 '오늘의 소재' 목록을 조회하는 API 이며 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVALID_PAGE", description = "페이지 번호는 0 이상이어야 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "사용자", hidden = true),
            @Parameter(name = "page", description = "페이지 번호, 0번이 1페이지 입니다.")
    })
    public ApiResponse<BookmarkResponseDTO.BookMarkMaterialListDTO> getBookmarks(@AuthUser long userId, @RequestParam Integer page) {
        if (page < 0) {
            throw new BookmarkHandler(ErrorStatus.INVALID_PAGE);
        }
        return ApiResponse.of(SuccessStatus._OK, BookmarkConverter.toBookMarkMaterialListDTO(bookmarkQueryService.getBookmarkMaterialList(userId, page)));
    }

}
