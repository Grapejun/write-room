package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeGoalsRepository;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyChallengeQueryServiceImpl implements MyChallengeQueryService{
    private final ChallengeRoutineRepository routineRepository;
    private final ChallengeGoalsRepository goalsRepository;
    private final ChallengeRoutineParticipationRepository routineParticipationRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;

    @Override
    public List<ChallengeRoutine> findChallengeRoutine(List<ChallengeRoutineParticipation> routineParticipationList) {

        return routineParticipationList.stream()
                .map(routineParticipation ->
                        routineParticipation.getChallengeRoutine()).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeRoutineParticipation> findChallengeRoutineParticipation(Long userId, Long roomId) {

        List<ChallengeRoutineParticipation> list = routineParticipationRepository.findByUserAndRoom(userQueryService.findUser(userId), roomQueryService.findRoom(roomId)).stream()
                .filter(routineParticipation -> routineParticipation.getChallengeStatus() != ChallengeStatus.PROGRESS).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<ChallengeGoals> findChallengeGoals(List<ChallengeGoalsParticipation> goalsParticipationList) {
        return goalsParticipationList.stream()
                .map(goalsParticipation ->
                        goalsParticipation.getChallengeGoals()).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeGoalsParticipation> findChallengeGoalsParticipation(Long userId, Long roomId) {
        List<ChallengeGoalsParticipation> list = goalsParticipationRepository.findByUserAndRoom(userQueryService.findUser(userId), roomQueryService.findRoom(roomId)).stream()
                .filter(goalsParticipation -> goalsParticipation.getChallengeStatus() != ChallengeStatus.PROGRESS).collect(Collectors.toList());

        return list;
    }
}
