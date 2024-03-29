package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ChallengeRoutineParticipationRepository extends JpaRepository<ChallengeRoutineParticipation, Long> {
    public ChallengeRoutineParticipation findByUserAndChallengeRoutine(User user, ChallengeRoutine routine);

    public List<ChallengeRoutineParticipation> findByUserAndRoom(User user, Room room);

    public List<ChallengeRoutineParticipation> findByChallengeRoutine(ChallengeRoutine routine);

    @Query("select crp from ChallengeRoutineParticipation crp where crp.user = :user and crp.room = :room and crp.challengeStatus = 'PROGRESS'")
    public ChallengeRoutineParticipation findProgressRoutineParticipation(@Param("user") User user, @Param("room") Room room);

    public List<ChallengeRoutineParticipation> findByChallengeStatus(ChallengeStatus challengeStatus);
}
