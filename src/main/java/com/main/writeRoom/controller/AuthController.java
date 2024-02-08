package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.service.AuthService.AuthService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO.UserSignInResult;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signIn")
    public ApiResponse<UserResponseDTO.UserSignInResult> signIn(@RequestBody UserRequestDTO.UserSignIn request) {
        UserSignInResult response = authService.login(request);
        return ApiResponse.of(SuccessStatus._OK, response);
    }

    @PostMapping("/signUp")
    public ApiResponse<UserResponseDTO.CustomUserInfo> addUser(@RequestBody UserRequestDTO.UserSignUp request) {
        User user = authService.join(request);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }

    @Operation(summary = "비밀번호 재설정 메일 전송 API", description = "비밀번호 재설정 메일 전송 API이며, 유저가 작성한 메일로 인증 메일을 전송합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PostMapping("/sendResetPwdEmail")
    public ApiResponse<UserResponseDTO.CustomUserInfo> sendResetPwd(@RequestBody UserRequestDTO.ResetPasswordForEmail request)
            throws MessagingException, UnsupportedJwtException {
        User user = authService.sendResetPwd(request);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }

    @Operation(summary = "비밀번호 재설정 API", description = "비밀번호 재설정 API이며, 유저가 재설정 버튼 url에 토큰을 같이 담았습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "재설정 토큰이 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "token", description = "비밀번호 재설정 토큰입니다.")
    })
    @PostMapping("/resetPwd")
    public ApiResponse<UserResponseDTO.CustomUserInfo> resetPwd(@RequestBody UserRequestDTO.ResetPassword request,
                                                                @RequestParam(name = "token")String resetToken) {
        User user = authService.resetPwd(request, resetToken);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }
}
