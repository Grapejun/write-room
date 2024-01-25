package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import java.time.LocalDate;
import java.util.List;

public interface ChallengeQueryService {

    public ChallengeRoutine findRoutine(Long challengeId);
    public List<LocalDate> findNoteDate(Long userId, Long challengeId);
}
