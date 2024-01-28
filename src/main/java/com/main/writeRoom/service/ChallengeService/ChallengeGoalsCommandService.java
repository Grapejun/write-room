package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;

import java.time.LocalDate;

public interface ChallengeGoalsCommandService {
    public ChallengeGoals create(Long roomId, ChallengeRequestDTO.ChallengeGoalsDTO request);
    public boolean deadlineRangeNull(LocalDate startDate, LocalDate deadline);
    public ChallengeGoalsParticipation giveUP(Long userId, Long challengeGoalsId);
}
