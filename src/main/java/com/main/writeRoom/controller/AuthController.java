package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.service.AuthService.AuthService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO.UserSignInResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
