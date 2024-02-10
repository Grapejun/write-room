package com.main.writeRoom.service.AuthService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.config.utils.JwtUtil;
import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.ExistedEmail;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.TokenHandler;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.ExistedEmailRespository;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.service.MailService.EmailService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final ExistedEmailRespository existedEmailRespository;
    private final EmailService emailService;

    @Transactional
    public UserResponseDTO.UserSignInResult login(UserRequestDTO.UserSignIn request) {

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_FOUND);
        }

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

    @Transactional
    public User sendResetPwd(UserRequestDTO.ResetPasswordForEmail request) throws MessagingException, UnsupportedJwtException {
        User user  = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new UserHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        emailService.sendEmail(request.getEmail(), user, resetToken, "password");
        return user;
    }

    @Transactional
    public User resetPwd(UserRequestDTO.ResetPassword request, String resetToken, String type) {
        User user = userRepository.findByResetToken(resetToken);
        String password = encoder.encode(request.getPassword());
        if (user == null) {
            throw new TokenHandler(ErrorStatus.TOKEN_NOT_FOUND);
        }

        ExistedEmail existedEmail = existedEmailRespository.findByResetToken(resetToken);
        if (Objects.equals(type, "pwd")) {
            user.setPassword(encoder.encode((request.getPassword())));
            user.setResetToken(null);
        } else {
            user.setEmail(existedEmail.getExistingEmail());
            existedEmailRespository.delete(existedEmail);

            user.setPassword(password);
            user.setResetToken(null);
        }
        return user;
    }
}
