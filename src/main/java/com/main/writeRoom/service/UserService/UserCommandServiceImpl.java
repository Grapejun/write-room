package com.main.writeRoom.service.UserService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.aws.s3.AmazonS3Manager;
import com.main.writeRoom.aws.s3.Uuid;
import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.ExistedEmail;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.*;
import com.main.writeRoom.service.MailService.EmailService;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import jakarta.mail.MessagingException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCommandServiceImpl implements UserCommandService {
    private final UserQueryService userQueryService;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final ExistedEmailRespository existedEmailRespository;

    private final UserRepository userRepository;
    private final ChallengeGoalsParticipationRepository challengeGoalsParticipationRepository;
    private final RoomParticipationRepository roomParticipationRepository;
    private final ChallengeRoutineParticipationRepository challengeRoutineParticipationRepository;
    private final NoteRepository noteRepository;



    @Override
    @Transactional
    public User updatedMyProfile(Long userId, UserRequestDTO.UpdatedMyprofile request, MultipartFile userImg) {
        User user = userQueryService.findUser(userId);

        String imgUrl = null;
        Uuid savedUuid = null;

        if (userImg != null && !userImg.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            imgUrl = s3Manager.uploadFile(s3Manager.generateReviewKeyName(savedUuid, "user"), userImg);
        }
        return user.setProfile(request.getNickName(), imgUrl);
    }

    @Transactional
    public User updatedPassword(Long userId, UserRequestDTO.UpdatedPassword request) {
        User user = userQueryService.findUser(userId);
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCH);
        }
        String updatedPwd = encoder.encode(request.getUpdatePwd());
        return user.setPassword(updatedPwd);
    }

    @Transactional
    public User updatedEmail(Long userId, UserRequestDTO.ResetPasswordForEmail request) throws MessagingException {
        User user = userQueryService.findUser(userId);
        User userEmail = userRepository.findByEmail(request.getEmail());

        if (userEmail != null) {
            throw new UserHandler(ErrorStatus.EXIST_EMAIL);
        }

        String resetToken = UUID.randomUUID().toString();

        user.setResetToken(resetToken);

        ExistedEmail existedEmail = UserConverter.toExistedEmailResult(user.getEmail(), resetToken, user);
        existedEmailRespository.save(existedEmail);

        emailService.sendEmail(request.getEmail(), user, resetToken, "email");
        user.setEmail(request.getEmail());

        return user;
    }

//    @Transactional
//    public void deleteUser(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
//        userRepository.delete(user);
//    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
        //챌린지 정보 지우기
        challengeGoalsParticipationRepository.deleteByUserId(userId);
        challengeRoutineParticipationRepository.deleteById(userId);
        //룸 참여 지우기
        roomParticipationRepository.deleteByUserId(userId);;
        //노트 지우기
        noteRepository.deleteByUserId(userId);
        // 사용자 정보 삭제
        userRepository.delete(user);
    }


}
