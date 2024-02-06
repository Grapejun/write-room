package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.IsActive;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeGoalsRepository;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyChallengeCommandServiceImpl implements MyChallengeCommandService{
    private final ChallengeRoutineRepository routineRepository;
    private final ChallengeRoutineParticipationRepository routineParticipationRepository;
    private final ChallengeGoalsRepository goalsRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;

    //나의 챌린지 루틴 내역 삭제 & 모든 참여자가 내역 삭제 했다면 delete
    @Override
    @Transactional
    public void inactiveRoutine(ChallengeRoutineParticipation routineParticipation) {
        routineParticipation.setIsActive(IsActive.INACTIVE);
        ChallengeRoutine routine= routineParticipation.getChallengeRoutine();
        List<ChallengeRoutineParticipation> routineParticipationList = routineParticipationRepository.findByChallengeRoutine(routine);
        for (ChallengeRoutineParticipation crp :
                routineParticipationList) {
            if (crp.getIsActive() == IsActive.ACTIVE)
                return;
        }
        routineParticipationRepository.deleteAll(routineParticipationList);
        routineRepository.delete(routine);
    }

    //나의 챌린지 목표량 내역 삭제
    @Override
    @Transactional
    public void inactiveGoals(ChallengeGoalsParticipation goalsParticipation) {
        goalsParticipation.setIsActive(IsActive.INACTIVE);
        ChallengeGoals goals= goalsParticipation.getChallengeGoals();
        List<ChallengeGoalsParticipation> goalsParticipationList = goalsParticipationRepository.findByChallengeGoals(goals);
        for (ChallengeGoalsParticipation cgp :
                goalsParticipationList) {
            if (cgp.getIsActive() == IsActive.ACTIVE)
                return;
        }
        goalsParticipationRepository.deleteAll(goalsParticipationList);
        goalsRepository.delete(goals);
    }
}
