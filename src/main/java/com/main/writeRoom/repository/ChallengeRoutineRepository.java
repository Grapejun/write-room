package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ChallengeRoutineRepository extends JpaRepository<ChallengeRoutine, Long> {
}
