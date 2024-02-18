package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeGoalsRepository extends JpaRepository<ChallengeGoals, Long> {
}
