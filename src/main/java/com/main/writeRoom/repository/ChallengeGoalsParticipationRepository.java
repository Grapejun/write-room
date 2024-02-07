package com.main.writeRoom.repository;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface ChallengeGoalsParticipationRepository extends JpaRepository<ChallengeGoalsParticipation, Long> {
    public ChallengeGoalsParticipation findByUserAndChallengeGoals(User user, ChallengeGoals goals);

    public List<ChallengeGoalsParticipation> findByUserAndRoom(User user, Room room);

    public List<ChallengeGoalsParticipation> findByChallengeGoals(ChallengeGoals goals);
    @Query("select cgp from ChallengeGoalsParticipation cgp where cgp.user = :user and cgp.room = :room and cgp.challengeStatus = 'PROGRESS'")
    public ChallengeGoalsParticipation findProgressGoalsParticipation(@Param("user") User user, @Param("room") Room room);
}
