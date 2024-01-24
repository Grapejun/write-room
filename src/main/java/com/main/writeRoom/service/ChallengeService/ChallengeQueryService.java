package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;

public interface ChallengeQueryService {

    public ChallengeRoutine read(Long userId);
}
