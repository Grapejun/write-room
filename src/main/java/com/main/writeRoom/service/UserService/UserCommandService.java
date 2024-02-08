package com.main.writeRoom.service.UserService;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {
    User updatedMyProfile(Long userId, UserRequestDTO.UpdatedMyprofile request, MultipartFile userImg);
}