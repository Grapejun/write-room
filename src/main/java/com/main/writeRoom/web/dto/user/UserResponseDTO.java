package com.main.writeRoom.web.dto.user;

import com.main.writeRoom.domain.enums.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomUserInfo {
        Long userId;
        String email;
        Role role;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomUserInfoDTO {
        Long userId;
        String email;
        String name;
        String password;
        Role role;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSignInResult {
        Long userId;
        String accessToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponseDTO {

        private int statusCode;
        private String message;
        private LocalDateTime timestamp;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyProfileDTO {
        private Long userId;
        String profileImg;
        String nickName;
        String email;
    }
}
