package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.converter.ChallengeParticipationConverter;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.handler.RoomHandler;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import com.main.writeRoom.repository.RoomRepository;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService{ //GET 외의 나머지 요청에 대한 로직

    private final ChallengeRoutineRepository challengeRoutineRepository;
    private final UserRepository userRepository;
    private final ChallengeRoutineParticipationRepository challengeRoutineParticipationRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public ChallengeRoutine create(Long roomId, ChallengeRequestDTO.ChallengeRoutineDTO request) {

        //룸 조회
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomHandler(ErrorStatus.ROOM_NOT_FOUND));
        ChallengeRoutine newChallengeRoutine = ChallengeConverter.toChallengeRoutine(room, request);

        //유저아이디로 유저 조회 -> 참여자
        List<User> userList = request.getUserList().stream()
                .map(user -> {
                    return userRepository.findById(user).orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
                }).collect(Collectors.toList());

        List<ChallengeRoutineParticipation> challengeRoutineParticipationList = ChallengeParticipationConverter.toChallengeRoutineParticipation(userList);
        challengeRoutineParticipationList.forEach(challengeRoutineParticipation -> {
            challengeRoutineParticipation.setChallengeRoutine(newChallengeRoutine);
            //챌린지 참여 테이블에 추가하는 코드
            challengeRoutineParticipationRepository.save(challengeRoutineParticipation);
        } );


        return challengeRoutineRepository.save(newChallengeRoutine);
    }
}
