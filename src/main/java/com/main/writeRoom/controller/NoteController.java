package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.service.NoteService.NoteCommandService;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
@Slf4j
public class NoteController {
    private final NoteQueryService noteQueryService;
    private final NoteCommandService noteCommandService;

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
            @Parameter(name = "user", description = "user", hidden = true),
    })
    @PostMapping("/bookmark/{roomId}/{noteId}/{userId}")
    public ApiResponse<NoteResponseDTO.NoteResult> noteBookmark(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "noteId")Long noteId, @AuthUser long user) {
        Note note = noteQueryService.findNote(noteId);
        noteCommandService.createBookmarkNote(roomId, note, user);
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
