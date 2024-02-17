package com.main.writeRoom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.service.UserService.UserCommandService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Operation(summary = "유저 프로필 조회 API", description = "유저의 프로필 조회 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/myProfile")
    public ApiResponse<UserResponseDTO.MyProfileDTO> getMyprofile(@AuthUser long userId) {
        User user = userQueryService.findUser(userId);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.MyprofileInfoResult(user));
    }

    @Operation(summary = "유저 프로필 수정 API", description = "유저의 프로필 수정 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping("/update/myProfile")
    public ApiResponse<UserResponseDTO.MyProfileDTO> updateMyprofile(@AuthUser long userId, @RequestParam(name = "request") String request,
                                                                     @RequestPart(required = false, value = "userImg") MultipartFile userImg) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        UserRequestDTO.UpdatedMyprofile jsonList = objectMapper.readValue(request, new TypeReference<>() {});
        User user = userCommandService.updatedMyProfile(userId, jsonList, userImg);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.MyprofileInfoResult(user));
    }

    @Operation(summary = "유저 비밀번호 변경 API", description = "유저의 비밀번호 변경 API이며, 입력한 기존 비밀번호와 데이터베이스 비밀번호가 일치 하지 않으면 예외 처리됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4003", description = "비밀번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
    })
    @PatchMapping("/password")
    public ApiResponse<UserResponseDTO.CustomUserInfo> updatedPassword(@AuthUser long userId, @RequestBody UserRequestDTO.UpdatedPassword request) {
        User user = userCommandService.updatedPassword(userId, request);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }

    @Operation(summary = "유저 이메일 주소 변경 API", description = "유저의 이메일 주소 변경 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4003", description = "비밀번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
    })
    @PatchMapping("/email")
    public ApiResponse<UserResponseDTO.CustomUserInfo> updatedEmail(@AuthUser long userId, @RequestBody UserRequestDTO.ResetPasswordForEmail request)
            throws MessagingException {
        User user = userCommandService.updatedEmail(userId, request);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }

    @Operation(summary = "유저 탈퇴 API", description = "유저의 탈퇴 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/delete")
    public ApiResponse<Void> deleteUser(@AuthUser long userId) {
        userCommandService.deleteUser(userId);
        return ApiResponse.of(SuccessStatus._OK, null );
    }






}
