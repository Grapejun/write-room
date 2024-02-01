package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeGoalsRepository extends JpaRepository<ChallengeGoals, Long> {
}
