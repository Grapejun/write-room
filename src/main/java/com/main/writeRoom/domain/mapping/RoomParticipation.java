package com.main.writeRoom.domain.mapping;

import static com.main.writeRoom.apiPayload.status.ErrorStatus.AUTHORITY_TYPE_ERROR;

import com.main.writeRoom.common.BaseEntity;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.handler.RoomHandler;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RoomParticipation")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoomParticipation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void updateAuthority(String authority) {
        if ("MANAGER".equals(authority)) {
            this.authority = Authority.MANAGER;
        } else if ("PARTICIPANT".equals(authority)) {
            this.authority = Authority.PARTICIPANT;
        } else {
            throw new RoomHandler(AUTHORITY_TYPE_ERROR);
        }
    }
}
