package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.converter.ChallengeParticipationConverter;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.handler.ChallengeHandler;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeGoalsRepository;
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
public class ChallengeGoalsCommandServiceImpl implements ChallengeGoalsCommandService{
    private final ChallengeGoalsRepository goalsRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;
    private final ChallengeGoalsQueryService goalsQueryService;
    private final RoomQueryService roomQueryService;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public ChallengeGoals create(Long roomId, ChallengeRequestDTO.ChallengeGoalsDTO request) {
        deadlineRangeNull(request.getStartDate(), request.getDeadline());

        Room room = roomQueryService.findRoom(roomId);
        ChallengeGoals newGoals = ChallengeConverter.toChallengeGoals(room, request);

        List<User> userList = request.getUserList().stream()
                .map(userId -> {
                    return userQueryService.findUser(userId);
                }).collect(Collectors.toList());

        List<ChallengeGoalsParticipation> goalsParticipationList = ChallengeParticipationConverter.toChallengeGoalsParticipation(userList);

        goalsParticipationList.forEach(goalsParticipation -> {
            goalsParticipation.setChallengeGoals(newGoals);
            goalsParticipation.setRoom(newGoals.getRoom());
            goalsParticipationRepository.save(goalsParticipation);
        });

        return goalsRepository.save(newGoals);
    }

    @Override
    public void deadlineRangeNull(LocalDate startDate, LocalDate deadline) {
        if (deadline == null) {
            return;
        } else {
            for (int i = 0; i < 4; i++) {
                if (deadline.isEqual(startDate.plusWeeks(i + 1).minusDays(1))) {
                    return;
                }
            }
            throw new ChallengeHandler(ErrorStatus.DEADLINE_OUT_RANGE);
        }
    }

    @Override
    @Transactional
    public ChallengeGoalsParticipation giveUP(Long userId, Long goalsId) {
        //회원, 챌린지 조회
        User user = userQueryService.findUser(userId);
        ChallengeGoals goals = goalsQueryService.findGoals(goalsId);

        //회원과 챌린지로 챌린지 참여 조회
        ChallengeGoalsParticipation goalsParticipation = goalsParticipationRepository.findByUserAndChallengeGoals(user, goals);
        if (goalsParticipation != null && goalsParticipation.getChallengeStatus() == ChallengeStatus.PROGRESS) {
            //챌린지 상태를 실패로 변경
            goalsParticipation.setChallengeStatus(ChallengeStatus.FAILURE);
            goalsParticipation.setStatusUpdatedAt(LocalDate.now());
        } else {
            throw new ChallengeHandler(ErrorStatus.PROGRESS_NOTFOUND);
        }

        return goalsParticipation;
    }
}
