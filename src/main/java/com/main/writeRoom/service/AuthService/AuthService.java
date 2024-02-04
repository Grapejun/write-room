package com.main.writeRoom.service.AuthService;

import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;

public interface AuthService {
    UserResponseDTO.UserSignInResult login(UserRequestDTO.UserSignIn request);
}
