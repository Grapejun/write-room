package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.IsActive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyChallengeCommandServiceImpl implements MyChallengeCommandService{

    //나의 챌린지 루틴 내역 삭제
    @Override
    @Transactional
    public void inactiveRoutine(ChallengeRoutineParticipation routineParticipation) {
        routineParticipation.setIsActive(IsActive.INACTIVE);
    }

    //나의 챌린지 목표량 내역 삭제
    @Override
    @Transactional
    public void inactiveGoals(ChallengeGoalsParticipation goalsParticipation) {
        goalsParticipation.setIsActive(IsActive.INACTIVE);
    }
}
