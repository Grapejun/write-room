package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRoutineParticipationRepository extends JpaRepository<ChallengeRoutineParticipation, Long> {
    ChallengeRoutineParticipation findByUserAndChallengeRoutine(User user, ChallengeRoutine routine);
}
