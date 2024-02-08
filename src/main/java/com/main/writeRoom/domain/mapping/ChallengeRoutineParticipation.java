package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ChallengeRoutineParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    private LocalDate statusUpdatedAt;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private IsActive isActive;

    @ColumnDefault("0")
    private Integer achieveCount; //달성한 노트가 몇 개인지 세기 위한 필드

    @ColumnDefault("false")
    private Boolean isNoteToday; //오늘 노트를 작성했는 지 표시하기 위한 필드

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

    public void setIsActive(IsActive isActive) {
        this.isActive = isActive;
    }

    public void plusAchieveCount() {
        this.achieveCount++;
    }

    public void setAchieveCount(int achieveCount) {
        this.achieveCount = achieveCount;
    }

    public void setIsNoteToday(boolean isNoteToday) {
        this.isNoteToday = isNoteToday;
    }
}
