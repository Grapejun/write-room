package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;

public interface ChallengeCommandService {

    public ChallengeRoutine create(Long roomId, ChallengeRequestDTO.ChallengeRoutineDTO request);
}
