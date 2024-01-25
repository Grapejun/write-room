package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.converter.ChallengeParticipationConverter;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.handler.ChallengeHandler;
import com.main.writeRoom.handler.RoomHandler;
import com.main.writeRoom.handler.UserHandler;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import com.main.writeRoom.repository.RoomRepository;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
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
    private final ChallengeRoutineParticipationRepository challengeRoutineParticipationRepository;
    private final ChallengeQueryService challengeQueryService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public ChallengeRoutine create(Long roomId, ChallengeRequestDTO.ChallengeRoutineDTO request) {

        //룸 조회
        Room room = roomQueryService.findRoom(roomId);
        //insert할 새 챌린지 엔티티로 변환
        ChallengeRoutine newChallengeRoutine = ChallengeConverter.toChallengeRoutine(room, request);

        //유저아이디로 유저 조회 -> 참여자
        List<User> userList = request.getUserList().stream()
                .map(userId -> {
                    return userQueryService.findUser(userId);
                }).collect(Collectors.toList());

        List<ChallengeRoutineParticipation> challengeRoutineParticipationList = ChallengeParticipationConverter.toChallengeRoutineParticipation(userList);

        challengeRoutineParticipationList.forEach(challengeRoutineParticipation -> {
            challengeRoutineParticipation.setChallengeRoutine(newChallengeRoutine);
            //챌린지 참여 테이블에 추가하는 코드
            challengeRoutineParticipationRepository.save(challengeRoutineParticipation);
        } );

        return challengeRoutineRepository.save(newChallengeRoutine);
    }

    @Override
    @Transactional
    public ChallengeRoutineParticipation giveUP(Long userId, Long routineId) {
        //회원, 챌린지 조회
        User user = userQueryService.findUser(userId);
        ChallengeRoutine routine = challengeQueryService.findRoutine(routineId);

        //회원과 챌린지로 챌린지 참여 조회
        ChallengeRoutineParticipation routineParticipation = challengeRoutineParticipationRepository.findByUserAndChallengeRoutine(user, routine);
        if (routineParticipation != null) {
            //챌린지 상태를 실패로 변경
            routineParticipation.setChallengeStatus(ChallengeStatus.FAILURE);
        } else {
            throw new ChallengeHandler(ErrorStatus.PARTICIPATION_NOTFOUND);
        }

        return routineParticipation;
    }


}
