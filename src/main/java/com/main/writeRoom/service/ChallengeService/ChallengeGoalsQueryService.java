package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;

public interface ChallengeGoalsQueryService {
    public ChallengeGoals findGoals(Long challengeId);
    public Integer findAchieveNote(User user, ChallengeGoals goals);
}
