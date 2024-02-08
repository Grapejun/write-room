package com.main.writeRoom.service.UserService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService{

    private final UserRepository userRepository;

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
