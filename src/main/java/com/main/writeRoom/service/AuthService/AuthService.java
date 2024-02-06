package com.main.writeRoom.service.AuthService;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;

public interface AuthService {
    UserResponseDTO.UserSignInResult login(UserRequestDTO.UserSignIn request);
    User join(UserRequestDTO.UserSignUp request);

    //UserResponseDTO.UserSignInResult kakaoLogin(String code);
}
