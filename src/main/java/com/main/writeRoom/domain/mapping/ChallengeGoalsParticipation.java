package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeGoalsParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_goals")
    private ChallengeGoals challengeGoals;

    public void setChallengeGoals(ChallengeGoals challengeGoals) {
        if (this.challengeGoals != null)
            challengeGoals.getChallengeGoalsParticipationList().remove(this);
        this.challengeGoals = challengeGoals;
        challengeGoals.getChallengeGoalsParticipationList().add(this);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChallengeStatus(ChallengeStatus challengeStatus) {
        this.challengeStatus = challengeStatus;
    }
}
