package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "RoomParticipation")
public class RoomParticipation {
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
}
