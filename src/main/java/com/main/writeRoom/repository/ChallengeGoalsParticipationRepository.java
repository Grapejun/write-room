package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeGoalsParticipationRepository extends JpaRepository<ChallengeGoalsParticipation, Long> {
}