package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;

import java.util.List;

public interface MyChallengeQueryService {
    public List<ChallengeRoutine> findChallengeRoutine(List<ChallengeRoutineParticipation> routineParticipationList);

    public List<ChallengeRoutineParticipation> findChallengeRoutineParticipation(Long userId, Long roomId);

    public List<ChallengeGoals> findChallengeGoals(List<ChallengeGoalsParticipation> goalsParticipationList);

    public List<ChallengeGoalsParticipation> findChallengeGoalsParticipation(Long userId, Long roomId);

    public ChallengeRoutineParticipation findByUserAndChallengeRoutine(User user, ChallengeRoutine routine);
    public ChallengeGoalsParticipation findByUserAndChallengeGoals(User user, ChallengeGoals goals);
}
