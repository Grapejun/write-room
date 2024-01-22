package com.main.writeRoom.domain.Challenge;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ChallengeRoutine")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChallengeRoutine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate deadline;
    private Integer targetCount;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @OneToMany(mappedBy = "challenge_routine")
    private List<ChallengeRoutineParticipation> challengeRoutineParticipationList = new ArrayList<>();
}