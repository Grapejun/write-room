package com.main.writeRoom.service.AuthService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.config.utils.JwtUtil;
import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO.UserSignInResult login(UserRequestDTO.UserSignIn request) {

//        User user = userRepository.findByEmail(request.getEmail());
//        if (user == null) {
//            throw new UserHandler(ErrorStatus.EMAIL_NOT_FOUND);
//        }
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (!userOptional.isPresent()) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_FOUND);
        }

        User user = userOptional.get();

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCH);
        }

        UserResponseDTO.CustomUserInfo info = UserConverter.CustomUserInfoResultDTO(user);

        String accessToken = jwtUtil.createAccessToken(info);

        return UserConverter.UserSignInResultDTO(user, accessToken);
    }

    @Transactional
    public User join(UserRequestDTO.UserSignUp request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserHandler(ErrorStatus.EXIST_EMAIL);
        }
        // 비밀번호 해시 처리
        String password = encoder.encode(request.getPassword());

        User user = UserConverter.UserSignUpDTO(request, password);
        return userRepository.save(user);
    }



}
