package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.service.CategoryService.CategoryQueryService;
import com.main.writeRoom.service.NoteService.NoteCommandService;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
@Slf4j
public class NoteController {
    private final NoteQueryService noteQueryService;
    private final NoteCommandService noteCommandService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;

    @Operation(summary = "노트 생성 API", description = "새로운 노트를 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "노트를 생성할 룸의 아이디입니다."),
    })
    @PostMapping("/rooms/{roomId}/notes")
    public ApiResponse<NoteResponseDTO.NoteResult> createNote(@PathVariable(name = "roomId")Long roomId, @RequestBody NoteRequestDTO.createNoteDTO request
    , @RequestPart(required = false, value = "roomImg") MultipartFile noteImg) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(request.getUserId());
        Category category = categoryQueryService.findCategory(request.getCategoryId());

        NoteResponseDTO.PreNoteResult preNote = noteCommandService.createPreNote(room, user, category, noteImg, request);
        Note note = noteCommandService.createNote(preNote);

        return ApiResponse.of(SuccessStatus._OK, NoteConverter.toNoteResult(note));
    }

    @Operation(summary = "노트 조회 API", description = "노트를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "조회할 노트가 있는 룸의 아이디입니다."),
            @Parameter(name = "noteId", description = "조회할 노트의 아이디입니다."),
    })
    @GetMapping("/rooms/{roomId}/notes/{noteId}")
    public ApiResponse<NoteResponseDTO.NoteResult> getNote(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "noteId")Long noteId) {

        // 노트 아이디 받아서 노트 조회
        Note note = noteQueryService.findNote(noteId);

        return ApiResponse.of(SuccessStatus._OK, noteCommandService.getNote(note));
    }

    @Operation(summary = "노트 수정 API", description = "노트를 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "수정할 노트가 있는 룸의 아이디입니다."),
            @Parameter(name = "noteId", description = "수정할 노트의 아이디입니다."),
    })
    @PatchMapping("/rooms/{roomId}/notes/{noteId}")
    public ApiResponse<NoteResponseDTO.NoteResult> updateNote(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "noteId")Long noteId) {
        return null;
    }

    @Operation(summary = "노트 삭제 API", description = "노트를 삭제하는 API입니다.") // 되는지 확인 해봐야 하고, 양방향 매핑도 삭제 해야 함.
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "삭제할 노트가 있는 룸의 아이디입니다."),
            @Parameter(name = "noteId", description = "삭제할 노트의 아이디입니다."),
    })
    @DeleteMapping("/rooms/{roomId}/notes/{noteId}")
    public ApiResponse deleteNote(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "noteId")Long noteId) {
        noteCommandService.deleteBookmarkNote(noteId);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "노트를 나의 북마크에 추가 API", description = "룸에 작성된 노트를 북마크에 추가하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTE4001", description = "노트가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "유저가 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
            @Parameter(name = "noteId", description = "노트 아이디 입니다."),
            @Parameter(name = "userId", description = "유ㅇ 아이디 입니다."),
    })
    @PostMapping("/bookmark/{roomId}/{noteId}/{userId}")
    public ApiResponse<NoteResponseDTO.NoteResult> noteBookmark(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "noteId")Long noteId, @PathVariable(name = "userId")Long userId) {
        Note note = noteQueryService.findNote(noteId);
        noteCommandService.createBookmarkNote(roomId, note, userId);
        return ApiResponse.of(SuccessStatus._OK, NoteConverter.toBookMarkNoteResult(note));
    }

    @Operation(summary = "북마크 노트 해제 API", description = "북마크한 노트를 해제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "bookmarkNoteId", description = "북마크 노트 아이디입니다."),
    })
    @DeleteMapping("/bookmark/delete/{bookmarkNoteId}")
    public ApiResponse deleteBookmark(@PathVariable(name = "bookmarkNoteId")Long bookmarkNoteId) {
        noteCommandService.deleteBookmarkNote(bookmarkNoteId);
        return ApiResponse.onSuccess();
    }
}
