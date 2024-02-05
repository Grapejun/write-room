package com.main.writeRoom.domain.mapping;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.Tag;
import jakarta.persistence.Entity;
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

@Entity(name = "NoteTag")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoteTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;

    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;
}
