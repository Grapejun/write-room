package com.main.writeRoom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.service.CategoryService.CategoryQueryService;
import com.main.writeRoom.service.EmojiService.EmojiQueryService;
import com.main.writeRoom.service.NoteService.NoteCommandService;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.note.NoteRequestDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.room.RoomRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class NoteController {
    private final NoteQueryService noteQueryService;
    private final NoteCommandService noteCommandService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;
    private final EmojiQueryService emojiQueryService;

    @Operation(summary = "노트 생성 API", description = "새로운 노트를 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            // 존재하지 않는 룸의 아이디를 입력했을 경우 에러
    })
    @Parameters({
            @Parameter(name = "roomId", description = "노트를 생성할 룸의 아이디입니다."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @PostMapping(value = "/rooms/{roomId}/notes", consumes = "multipart/form-data")
    public ApiResponse<NoteResponseDTO.NoteResult> createNote(@AuthUser long userId,
                                                              @PathVariable(name = "roomId")Long roomId,
                                                              @RequestParam(name = "request") String request,
                                                              @RequestPart(required = false, value = "noteImg") MultipartFile noteImg)
            throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        NoteRequestDTO.createNoteDTO jsonList = objectMapper.readValue(request, new TypeReference<>() {});
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        Category category = categoryQueryService.findCategory(jsonList.getCategoryId());

        NoteResponseDTO.PreNoteResult preNote = noteCommandService.createPreNote(room, user, category, noteImg, jsonList); //여기에 챌린지 달성 코드 추가
        Note note = noteCommandService.createNote(preNote);

        return getNote(note.getId());
    }

    @Operation(summary = "노트 조회 API", description = "노트를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            // 존재 하지 않는 노트일 때 에러
            // 조회에서도 룸 안의 사람만 조회 가능한가? 필요하면 조건 추가.
            // 노트 생성 수정 시 바로 노트 조회 반환해도 좋을 듯 함.
    })
    @Parameters({
            @Parameter(name = "noteId", description = "조회할 노트의 아이디입니다."),
    })
    @GetMapping("/notes/{noteId}")
    public ApiResponse<NoteResponseDTO.NoteResult> getNote(@PathVariable(name = "noteId")Long noteId) {

        Note note = noteQueryService.findNote(noteId);
        List<EmojiClick> emojiClickList = emojiQueryService.findAllByNote(note);

        // 노트에 해당하는 이모지 전체 조회
        return ApiResponse.of(SuccessStatus._OK, noteQueryService.getNote(note, emojiClickList));
    }

    // 조회 페이지를 먼저 들어가서 수정 해야 함. 수정시에는 노트 아이디와 수정 컨텐츠를 같이 받을 것
    @Operation(summary = "노트 수정 API", description = "노트를 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            // 형식이 맞지 않을 때 에러
            // 존재 하지 않는 노트일 때 에러
            // 작성자가 아닌 경우 수정 불가
    })
    @PutMapping(value = "/notes/{noteId}", consumes = "multipart/form-data")
    public ApiResponse<NoteResponseDTO.NoteResult> updateNote(
            @PathVariable(name = "noteId")Long noteId,
            @RequestParam(name = "request") String request,
            @RequestPart(required = false, value = "noteImg") MultipartFile noteImg)
            throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        NoteRequestDTO.patchNoteDTO jsonList = objectMapper.readValue(request, new TypeReference<>() {});
        // 노트가 존재하지 않으면 에러
        Note note = noteQueryService.findNote(noteId);
        // 사용자의 노트가 아닐 경우 에러
        Category category = categoryQueryService.findCategory(jsonList.getCategoryId());
        Note updatedNote = noteCommandService.updateNoteFields(note, category, noteImg, jsonList);
        return getNote(updatedNote.getId());
    }

    @Operation(summary = "노트 삭제 API", description = "노트를 삭제하는 API입니다.") // 되는지 확인 해봐야 하고, 양방향 매핑도 삭제 해야 함. 이모지 태그 등
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "noteId", description = "삭제할 노트의 아이디입니다."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @DeleteMapping("/notes/{noteId}")
    public ApiResponse<NoteResponseDTO.NoteDeleteResult> deleteNote(
            @PathVariable(name = "noteId")Long noteId,
            @AuthUser long userId) {
        User user = userQueryService.findUser(userId);
        return ApiResponse.of(SuccessStatus._OK, noteCommandService.deleteNote(noteId, user));
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
            @Parameter(name = "user", description = "user", hidden = true),
    })
    @PostMapping("/notes/bookmark/{roomId}/{noteId}")
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
    @DeleteMapping("/notes/bookmark/delete/{bookmarkNoteId}")
    public ApiResponse deleteBookmark(@PathVariable(name = "bookmarkNoteId")Long bookmarkNoteId) {
        noteCommandService.deleteBookmarkNote(bookmarkNoteId);
        return ApiResponse.onSuccess();
    }
}
