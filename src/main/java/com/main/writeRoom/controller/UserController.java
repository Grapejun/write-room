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
import com.main.writeRoom.service.UserService.UserCommandService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("/update/myProfile")
    public ApiResponse<UserResponseDTO.MyProfileDTO> updateMyprofile(@AuthUser long userId, @RequestParam(name = "request") String request,
                                                                     @RequestPart(required = false, value = "userImg") MultipartFile userImg) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        UserRequestDTO.UpdatedMyprofile jsonList = objectMapper.readValue(request, new TypeReference<>() {});
        User user = userCommandService.updatedMyProfile(userId, jsonList, userImg);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.MyprofileInfoResult(user));
    }
}
