package com.main.writeRoom.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequestDTO {
    @Getter
    public static class UserSignIn {
        @NotNull(message = "이메일 입력은 필수입니다.")
        @Email
        String email;
        @NotNull(message = "비밀번호 입력은 필수입니다.")
        String password;
    }
}
