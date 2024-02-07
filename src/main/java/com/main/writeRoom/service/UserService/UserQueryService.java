package com.main.writeRoom.service.UserService;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.web.dto.user.UserResponseDTO;

public interface UserQueryService {
    User findUser(Long userId);

}


