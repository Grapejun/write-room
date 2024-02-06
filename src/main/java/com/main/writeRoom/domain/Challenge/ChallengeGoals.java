package com.main.writeRoom.domain.Challenge;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ChallengeGoals")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeGoals extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer targetCount;
    private LocalDate startDate;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @OneToMany(mappedBy = "challengeGoals")
    private List<ChallengeGoalsParticipation> challengeGoalsParticipationList = new ArrayList<>();

    @Override
    public String toString() {
        return targetCount+"개 글쓰기";
    }

    public List<User> getParticipantList() { //챌린지 루틴과 목표량의 참여 타입을 User로 통일해야해서 만듦.
        return challengeGoalsParticipationList.stream()
                .map(ChallengeGoalsParticipation::getUser)
                .collect(Collectors.toList());
    }
}
