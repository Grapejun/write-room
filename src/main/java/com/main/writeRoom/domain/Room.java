package com.main.writeRoom.domain;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Duration;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomParticipation> roomParticipationList = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Note> noteList = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Category> categoryList = new ArrayList<>();

    public String daysSinceLastUpdate() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(this.getUpdatedAt(), now);

        if (duration.toDays() == 0) {
            long hourDifference = duration.toHours();
            if (hourDifference == 0) {
                return "방금 전";
            } else {
                return hourDifference + "시간 전";
            }
        } else {
            return duration.toDays() + "일 전";
        }
    }
    public Room setRoomInfo(String roomTitle, String roomIntroduction, String coverImg) {
        this.title = roomTitle;
        this.introduction = roomIntroduction;
        this.coverImg = coverImg;
        return this;
    }
}
