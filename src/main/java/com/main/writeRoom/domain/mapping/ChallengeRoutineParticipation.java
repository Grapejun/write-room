package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
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
public class ChallengeRoutineParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    private LocalDate statusUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_routine")
    private ChallengeRoutine challengeRoutine;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    public void setChallengeRoutine(ChallengeRoutine challengeRoutine) {
        if (this.challengeRoutine != null)
            challengeRoutine.getChallengeRoutineParticipationList().remove(this);
        this.challengeRoutine = challengeRoutine;
        challengeRoutine.getChallengeRoutineParticipationList().add(this);
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
