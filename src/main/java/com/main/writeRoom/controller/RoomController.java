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
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Category;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.service.CategoryService.CategoryQueryService;
import com.main.writeRoom.service.ChallengeService.ChallengeGoalsQueryService;
import com.main.writeRoom.service.ChallengeService.ChallengeRoutineQueryService;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.service.RoomParticipationService.RoomParticipationService;
import com.main.writeRoom.service.RoomService.RoomCommandService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.validation.annotation.PageLessNull;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.room.RoomRequestDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO;
import com.main.writeRoom.web.dto.room.RoomResponseDTO.MyRoomResultDto;
import com.main.writeRoom.web.dto.room.roomPaticipation.userRoomResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Slf4j
public class RoomController {
    private final RoomCommandService roomCommandService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final NoteQueryService noteQueryService;
    private final CategoryQueryService categoryQueryService;
    private final RoomParticipationService roomParticipationService;
    private final ChallengeRoutineQueryService routineQueryService;
    private final ChallengeGoalsQueryService goalsQueryService;

    @GetMapping("/myRoomList")
    @Operation(summary = "나의 룸 목록 조회 API", description = "해당 유저가 참여중인 룸의 목록들을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "Page는 0부터 입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON401", description = "인증이 필요합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다.")
    })
    public ApiResponse<List<MyRoomResultDto>> myRoomList(@AuthUser long user, @PageLessNull @RequestParam(name = "page") Integer page) {
        Page<RoomParticipation> room = roomCommandService.getMyRoomResultList(user, page);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.myRoomListInfoDTO(room));
    }

    @Operation(summary = "룸 멤버 조회 API", description = "해당 룸의 참여중인 멤버를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 인덱스 입니다.")
    })
    @GetMapping("/{roomId}/userRoom")
    public ApiResponse<userRoomResponseDTO.getUserRoom> getUserRoomInfoList(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        RoomParticipation roomParticipation = roomCommandService.getUserRoomInfo(room, user);
        Page<RoomParticipation> roomParticipationList = roomCommandService.getUserRoomInfoList(room);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toUserRoomResultDTO(roomParticipation, roomParticipationList));
    }

    @Operation(summary = "룸 떠나기 API", description = "룸 떠나기 API이며, 모든 권한의 유저가 룸을 떠날 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 인덱스 입니다.")
    })
    @DeleteMapping("/{roomId}")
    public ApiResponse leaveRoom(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);

        roomParticipationService.leaveRoom(room, user);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "룸 멤버 내보내기 API", description = "룸 멤버 내보내기 API이며, MANAGER(관리자)권한을 가진 유저만 룸 멤버를 내보낼 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORITY4001", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 인덱스 입니다."),
            @Parameter(name = "outUserId", description = "내보낼 유저의 인덱스 입니다.")
    })
    @DeleteMapping("/{roomId}/{outUserId}")
    public ApiResponse outRoom(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId, @PathVariable(name = "outUserId")Long outUserId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        User outUser = userQueryService.findUser(outUserId);

        roomParticipationService.outRoom(room, user, outUser);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "룸 멤버 권한 변경 API", description = "룸 멤버 권한 변경 API이며, MANAGER(관리자)권한을 가진 유저만 룸 멤버의 권한을 변경할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORITY4001", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORITY4002", description = "올바른 권한 형식을 입력하세요.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 인덱스 입니다."),
            @Parameter(name = "updateId", description = "권한이 변경될 유저의 인덱스 입니다.")
    })
    @PatchMapping("/authority/{roomId}/{updateId}")
    public ApiResponse updateAuthority(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId, @PathVariable(name = "updateId")Long updateId, @RequestParam(name = "authority")String authority) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        User updateUser = userQueryService.findUser(updateId);

        roomParticipationService.updateAuthority(room, user, updateUser, authority);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "룸 생성 API", description = "룸을 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @PostMapping(value = "/createRoom", consumes = "multipart/form-data")
    public ApiResponse<RoomResponseDTO.RoomInfoResult> CreateRoom(@AuthUser long userId, @RequestParam(name = "request") String request,
                                                                  @RequestPart(required = false, value = "roomImg")MultipartFile roomImg)
            throws JsonProcessingException {
        User user = userQueryService.findUser(userId);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        RoomRequestDTO.CreateRoomDTO jsonList = objectMapper.readValue(request, new TypeReference<>() {});
        Room room = roomCommandService.createRoom(user, jsonList, roomImg);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(room));
    }

    @Operation(summary = "룸 삭제 API", description = "룸을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORITY4001", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 인덱스 입니다.")
    })
    @DeleteMapping("/delete/{roomId}")
    public ApiResponse deleteRoom(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);

        roomCommandService.deleteRoom(room, user);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "해당 룸에 대한 노트 목록 조회 API", description = "해당 룸에 대한 노트 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다."),
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
    })
    @GetMapping("/{roomId}/list")
    public ApiResponse<NoteResponseDTO.RoomResult> getNoteList(@PathVariable(name = "roomId")Long roomId, @PageLessNull @RequestParam(name = "page") Integer page) {
        Room room = roomQueryService.findRoom(roomId);
        Page<Note> note = noteQueryService.getNoteListForRoom(room, page);
        return ApiResponse.of(SuccessStatus._OK, NoteConverter.toRoomResultDTO(room, note));
    }

    @Operation(summary = "룸에 참여중인 멤버들의 최근 수정일자 조회 API", description = "해당 룸에 참여중인 멤버들의 노트 수정일자를 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다."),
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
    })
    @GetMapping("/updateAt/{roomId}")
    public ApiResponse<List<userRoomResponseDTO.getUpdatedAtUserList>> getUpdateAtUserList(@PathVariable(name = "roomId")Long roomId, @PageLessNull @RequestParam(name = "page") Integer page) {
        Room room = roomQueryService.findRoom(roomId);
        Page<RoomParticipation> userRoomList = roomCommandService.findUpdateAtUserList(room, page);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.updateAtUserList(userRoomList));
    }

    @Operation(summary = "카테고리에 속한 노트 목록 조회 API", description = "카테고리에 속한 노트 리스트를 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CATEGORY4001", description = "카테고리가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다."),
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
            @Parameter(name = "categoryId", description = "카테고리 아이디 입니다.")
    })
    @GetMapping("/noteList/{roomId}/{categoryId}")
    public ApiResponse<NoteResponseDTO.RoomResult> getNoteListForCategory(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "categoryId")Long categoryId, @PageLessNull @RequestParam(name = "page") Integer page) {
        Room room = roomQueryService.findRoom(roomId);
        Category category = categoryQueryService.findCategory(categoryId);
        Page<Note> note = noteQueryService.findNoteForRoomAndCategory(category, room, page);
        return ApiResponse.of(SuccessStatus._OK, NoteConverter.toRoomResultDTO(room, note));
    }

    @Operation(summary = "룸 참여 API", description = "룸에 참여하는 API이며, 참여하기 버튼을 통해 참여하면 자동으로 권한은 PARTICIPANT입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 아이디 입니다.")
    })
    @PostMapping("/roomParticipation/{roomId}")
    public ApiResponse<RoomResponseDTO.RoomInfoResult> roomParticipationUser(@PathVariable(name = "roomId")Long roomId, @AuthUser long userId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        Room response = roomCommandService.roomParticipateIn(room, user);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toCreateRoomResultDTO(response));
    }

    //챌린지 달성률 조회
    @GetMapping("/challenges/{roomId}")
    @Operation(summary = "챌린지 달성률 조회 API", description = "룸에서 진행 중인 챌린지 루틴과 챌린지 목표량의 달성률을 조회하는 API입니다. 진행 중인 챌린지가 없다면 null과 0을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 아이디 입니다.")
    })
    public ApiResponse<RoomResponseDTO.ChallengeAchieveResult> getChallengesAchieve(@AuthUser long user, @PathVariable(name = "roomId") Long roomId) {
        User user1 = userQueryService.findUser(user);
        Room room = roomQueryService.findRoom(roomId);
        ChallengeRoutineParticipation crp = routineQueryService.findProgressRoutineParticipation(user1, room);
        ChallengeGoalsParticipation cgp = goalsQueryService.findProgressGoalsParticipation(user1, room);
        return ApiResponse.of(SuccessStatus._OK, RoomConverter.toChallengeAchieveResult(crp, cgp));
    }
}
