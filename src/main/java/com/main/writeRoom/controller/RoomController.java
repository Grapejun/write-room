package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.RoomConverter;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.service.RoomParticipationService.RoomParticipationService;
import com.main.writeRoom.service.RoomService.RoomCommandService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.validation.annotation.PageLessNull;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Slf4j
public class RoomController {
    private final RoomCommandService roomCommandService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;
    private final RoomParticipationService roomParticipationService;

    @GetMapping("/{userId}")
    @Operation(summary = "나의 룸 목록 조회 API", description = "해당 유저가 참여중인 룸의 목록들을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "Page는 0부터 입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다."),
    })
    public ApiResponse<List<MyRoomResultDto>> myRoomList(@PathVariable(name = "userId") Long userId, @PageLessNull @RequestParam(name = "page") Integer page) {
        Page<RoomParticipation> room = roomCommandService.getMyRoomResultList(userId, page);
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
    @GetMapping("/{roomId}/{userId}/userRoom")
    public ApiResponse<userRoomResponseDTO.getUserRoom> getUserRoomInfoList(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "userId")Long userId) {
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
    @DeleteMapping("/{roomId}/{userId}")
    public ApiResponse leaveRoom(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "userId")Long userId) {
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
    @DeleteMapping("/{roomId}/{userId}/{outUserId}")
    public ApiResponse outRoom(@PathVariable(name = "roomId")Long roomId, @PathVariable(name = "userId")Long userId, @PathVariable(name = "outUserId")Long outUserId) {
        Room room = roomQueryService.findRoom(roomId);
        User user = userQueryService.findUser(userId);
        User outUser = userQueryService.findUser(outUserId);

        roomParticipationService.outRoom(room, user, outUser);
        return ApiResponse.onSuccess();
    }
}
