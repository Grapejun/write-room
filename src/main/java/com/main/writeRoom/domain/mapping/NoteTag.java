package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "NoteTag")
public class NoteTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;
}
