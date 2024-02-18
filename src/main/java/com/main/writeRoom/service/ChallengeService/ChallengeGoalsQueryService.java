package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;

import java.util.List;

public interface ChallengeGoalsQueryService {
    public ChallengeGoals findGoals(Long challengeId);
    public ChallengeGoalsParticipation findProgressGoalsParticipation(User user, Room room);
    public List<ChallengeGoalsParticipation> findByChallengeStatus(ChallengeStatus challengeStatus);

    public List<User> findGoalsUsers(Room room);
}
