package com.main.writeRoom.converter;

import com.main.writeRoom.domain.User.ExistedEmail;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.enums.Role;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;

public class UserConverter {

    public static UserResponseDTO.UserSignInResult UserSignInResultDTO(User user, String accessToken) {
        return UserResponseDTO.UserSignInResult.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }

    public static UserResponseDTO.CustomUserInfoDTO CustomUserResultDTO(User user) {
        return UserResponseDTO.CustomUserInfoDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static UserResponseDTO.CustomUserInfo CustomUserInfoResultDTO(User user) {
        return UserResponseDTO.CustomUserInfo.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User UserSignUpDTO(UserRequestDTO.UserSignUp request, String password) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getNickName())
                .password(password)
                .role(Role.USER)
                .build();
    }

    public static UserResponseDTO.MyProfileDTO MyprofileInfoResult(User user) {
        return UserResponseDTO.MyProfileDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getName())
                .profileImg(user.getProfileImage())
                .build();
    }

    public static ExistedEmail toExistedEmailResult (String email, String resetToken, User user) {
        return ExistedEmail.builder()
                .existingEmail(email)
                .resetToken(resetToken)
                .user(user)
                .build();
    }
}
