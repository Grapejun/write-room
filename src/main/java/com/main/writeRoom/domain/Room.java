package com.main.writeRoom.domain;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Room")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String introduction;
    private String url;
    private String coverImg;

    @OneToMany(mappedBy = "room")
    List<ChallengeRoutine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomParticipation> roomParticipationList = new ArrayList<>();
}
