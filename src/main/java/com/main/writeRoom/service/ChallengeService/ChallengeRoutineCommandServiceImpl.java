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
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeRoutineCommandServiceImpl implements ChallengeRoutineCommandService { //GET 외의 나머지 요청에 대한 로직

    private final ChallengeRoutineRepository challengeRoutineRepository;
    private final ChallengeRoutineParticipationRepository challengeRoutineParticipationRepository;
    private final ChallengeRoutineQueryService routineQueryService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public ChallengeRoutine create(Long roomId, ChallengeRequestDTO.ChallengeRoutineDTO request) {
       deadlineRange(request.getStartDate(), request.getDeadline());

        //룸 조회
        Room room = roomQueryService.findRoom(roomId);

        //insert할 새 챌린지 엔티티로 변환
        ChallengeRoutine newChallengeRoutine = ChallengeConverter.toChallengeRoutine(room, request);

        //유저아이디로 유저 조회 -> 참여자
        List<User> userList = request.getUserList().stream()
                .map(userId -> {
                    User user = userQueryService.findUser(userId);
                    if(routineQueryService.findProgressRoutineParticipation(user, room) != null) //참여자로 등록한 유저 중에 이미 챌린지를 진행하고 있는 유저가 있는지 검사
                        throw new ChallengeHandler(ErrorStatus.ALREADY_PROGRESS_2);
                    return user;
                }).collect(Collectors.toList());

        List<ChallengeRoutineParticipation> challengeRoutineParticipationList = ChallengeParticipationConverter.toChallengeRoutineParticipation(userList);

        challengeRoutineParticipationList.forEach(challengeRoutineParticipation -> {
            challengeRoutineParticipation.setChallengeRoutine(newChallengeRoutine);
            challengeRoutineParticipation.setRoom(newChallengeRoutine.getRoom());
            //챌린지 참여 테이블에 추가하는 코드
            challengeRoutineParticipationRepository.save(challengeRoutineParticipation);
        } );

        return challengeRoutineRepository.save(newChallengeRoutine);
    }

    @Override
    public void deadlineRange(LocalDate startDate, LocalDate deadline) {
        for (int i = 0; i < 4; i++) {
            if (deadline.isEqual(startDate.plusWeeks(i + 1).minusDays(1))) {
                return;
            }
        }
        throw new ChallengeHandler(ErrorStatus.DEADLINE_OUT_RANGE);
    }


    @Override
    @Transactional
    public ChallengeRoutineParticipation giveUp(Long userId, Long routineId) {
        //회원, 챌린지 조회
        User user = userQueryService.findUser(userId);
        ChallengeRoutine routine = routineQueryService.findRoutine(routineId);

        //회원과 챌린지로 챌린지 참여 조회
        ChallengeRoutineParticipation routineParticipation = challengeRoutineParticipationRepository.findByUserAndChallengeRoutine(user, routine);
        if (routineParticipation != null && routineParticipation.getChallengeStatus() == ChallengeStatus.PROGRESS) {
            //챌린지 상태를 포기로 변경
            routineParticipation.setChallengeStatus(ChallengeStatus.GIVEUP);
            routineParticipation.setStatusUpdatedAt(LocalDate.now());
        } else {
            throw new ChallengeHandler(ErrorStatus.PROGRESS_NOTFOUND);
        }

        return routineParticipation;
    }
}
