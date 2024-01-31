package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDate statusUpdatedAt; //나의 챌린지에서 endDate를 위한 필드

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_goals")
    private ChallengeGoals challengeGoals;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

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

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setStatusUpdatedAt(LocalDate localDate) {
        this.statusUpdatedAt = localDate;
    }
}
