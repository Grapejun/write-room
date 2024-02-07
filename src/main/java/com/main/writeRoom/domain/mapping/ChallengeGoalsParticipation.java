package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
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
public class ChallengeGoalsParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    private LocalDate statusUpdatedAt; //나의 챌린지에서 endDate를 위한 필드

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private IsActive isActive;  //나의 챌린지에서 내역 삭제를 하면 비활성화가 되어 내역에서 안보인다. 내역에선 안뜨고, 다른 회원이 봤을 땐 참여자로 떠야하기 때문. -> 참여자 모두 포기한 상태면 db에서 챌린지 참여와 챌린지 삭제

    @ColumnDefault("0")
    private Integer achieveCount; //달성한 노트가 몇 개인지 세기 위한 필드

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

    public void setIsActive(IsActive isActive) {
        this.isActive = isActive;
    }

    public void plusAchieveCount() {
        this.achieveCount++;
    }
}
