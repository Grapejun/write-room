package com.main.writeRoom.service.AuthService;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import jakarta.mail.MessagingException;

public interface AuthService {
    UserResponseDTO.UserSignInResult login(UserRequestDTO.UserSignIn request);
    User join(UserRequestDTO.UserSignUp request);
    User sendResetPwd(UserRequestDTO.ResetPasswordForEmail request) throws MessagingException;
    User resetPwd(UserRequestDTO.ResetPassword request, String resetToken, String type);

    //UserResponseDTO.UserSignInResult kakaoLogin(String code);
}
