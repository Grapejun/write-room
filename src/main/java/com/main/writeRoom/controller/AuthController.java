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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "비밀번호 재설정 API", description = "로그인 시 비밀번호 재설정 API이며, 유저가 재설정 버튼 url에 토큰을 같이 담았습니다. "
            + "type 을 입력해주세요. ex) 1. email // 2. pwd")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TOKEN4001", description = "재설정 토큰이 잘못되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4003", description = "이미 존재하는 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "token", description = "비밀번호 재설정 토큰입니다.")
    })
    @PostMapping("/resetPwd")
    public ApiResponse<UserResponseDTO.CustomUserInfo> resetPwd(@RequestBody UserRequestDTO.ResetPassword request,
                                                                @RequestParam(name = "token")String resetToken,
                                                                @RequestParam(name= "type")String type) {
        User user = authService.resetPwd(request, resetToken, type);
        return ApiResponse.of(SuccessStatus._OK, UserConverter.CustomUserInfoResultDTO(user));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok("Logged out successfully.");
    }
}
