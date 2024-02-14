package com.main.writeRoom.service.UserService;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {
    User updatedMyProfile(Long userId, UserRequestDTO.UpdatedMyprofile request, MultipartFile userImg);
    User updatedPassword(Long userId, UserRequestDTO.UpdatedPassword request);
    User updatedEmail(Long userId, UserRequestDTO.ResetPasswordForEmail request) throws MessagingException;
    void deleteUser(Long userId);
}