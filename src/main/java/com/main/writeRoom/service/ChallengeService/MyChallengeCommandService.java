package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;

public interface MyChallengeCommandService {
    public void inactiveRoutine(ChallengeRoutineParticipation routineParticipation);

    public void inactiveGoals(ChallengeGoalsParticipation goalsParticipation);
}
