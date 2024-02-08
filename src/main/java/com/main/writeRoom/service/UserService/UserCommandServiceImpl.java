package com.main.writeRoom.service.UserService;

import com.main.writeRoom.aws.s3.AmazonS3Manager;
import com.main.writeRoom.aws.s3.Uuid;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.repository.UuidRepository;
import com.main.writeRoom.web.dto.user.UserRequestDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
}
