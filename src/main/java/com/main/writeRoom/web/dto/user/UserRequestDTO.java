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

    @Getter
    public static class UserSignUp{
        @Email
        @NotNull(message = "이메일 입력은 필수입니다.")
        String email;

        @NotNull(message = "비밀번호 입력은 필수입니다.")
        String password;

        @NotNull(message = "닉네임 입력은 필수입니다.")
        String nickName;
    }

    @Getter
    public static class UpdatedMyprofile {
        String nickName;
    }

    @Getter
    public static class UpdatedPassword {
        @NotNull(message = "기존 비밀번호 입력은 필수입니다.")
        String password;
        @NotNull(message = "변경 비밀번호 입력은 필수입니다.")
        String updatePwd;
    }

    @Getter
    public static class ResetPasswordForEmail {
        @NotNull(message = "이메일 입력은 필수입니다.")
        @Email
        String email;
    }

    @Getter
    public static class ResetPassword {
        @NotNull(message = "비밀번호 입력은 필수입니다.")
        String password;
    }
}
