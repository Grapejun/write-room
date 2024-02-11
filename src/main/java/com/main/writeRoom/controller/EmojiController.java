package com.main.writeRoom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.EmojiConverter;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Emoji;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.handler.BookmarkHandler;
import com.main.writeRoom.handler.EmojiHandler;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.service.EmojiService.EmojiCommandService;
import com.main.writeRoom.service.EmojiService.EmojiQueryService;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.emoji.EmojiResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Emoji")
@Slf4j
public class EmojiController {
    private final EmojiQueryService emojiQueryService;
    private final EmojiCommandService emojiCommandService;
    private final NoteQueryService noteQueryService;
    private final UserQueryService userQueryService;
    @Operation(summary = "이모지 남기기 API", description = "새로운 이모지를 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMOJI4002", description = "이미 해당 노트에 이모지를 등록 했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMOJI4003", description = "이모지 넘버는 1~6 까지만 등록 가능 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "noteId", description = "이모지를 남길 노트의 아이디입니다."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @PostMapping(value = "/{noteId}")
    public ApiResponse<EmojiResponseDTO.EmojiClickResult> createEmoji(@AuthUser long userId,
                                                              @PathVariable(name = "noteId")Long noteId,
                                                              @RequestParam(name = "emojiNum") Long emojiNum)
    {
        if (emojiNum < 1 || emojiNum > 6) {
            throw new EmojiHandler(ErrorStatus.EMOJI_NUM_RANGE_ERROR);
        }
        Note note = noteQueryService.findNote(noteId);
        User user = userQueryService.findUser(userId);

        return ApiResponse.of(SuccessStatus._OK, emojiCommandService.postEmoji(note, user, emojiNum));
    }
    @Operation(summary = "이모지 수정 API", description = "이모지를 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMOJI4001", description = "사용자가 남긴 이모지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMOJI4003", description = "이모지 넘버는 1~6 까지만 등록 가능 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "noteId", description = "이모지를 수정할 노트의 아이디입니다."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @PatchMapping(value = "/{noteId}")
    public ApiResponse<EmojiResponseDTO.EmojiUpdateResult> updateEmoji(@AuthUser long userId,
                                                                      @PathVariable(name = "noteId")Long noteId,
                                                                      @RequestParam(name = "emojiNum") Long emojiNum)
    {
        if (emojiNum < 1 || emojiNum > 6) {
            throw new EmojiHandler(ErrorStatus.EMOJI_NUM_RANGE_ERROR);
        }
        Note note = noteQueryService.findNote(noteId);
        User user = userQueryService.findUser(userId);
        EmojiClick emojiClick = emojiQueryService.findByNoteAndUser(note, user);

        return ApiResponse.of(SuccessStatus._OK, emojiCommandService.updateEmoji(note, user, emojiClick, emojiNum));
    }
    @Operation(summary = "이모지 삭제 API", description = "이모지를 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTE4001", description = "노트가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EMOJI4001", description = "사용자가 남긴 이모지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "noteId", description = "이모지를 삭제할 노트의 아이디입니다."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @DeleteMapping(value = "/{noteId}")
    public ApiResponse<EmojiResponseDTO.EmojiDeleteResult> deleteEmoji(@AuthUser long userId,
                                                                      @PathVariable(name = "noteId")Long noteId)
    {
        Note note = noteQueryService.findNote(noteId);
        User user = userQueryService.findUser(userId);
        EmojiClick emojiClick = emojiQueryService.findByNoteAndUser(note, user);

        return ApiResponse.of(SuccessStatus._OK, emojiCommandService.deleteEmoji(emojiClick));
    }

}
