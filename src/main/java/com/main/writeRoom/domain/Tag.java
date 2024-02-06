package com.main.writeRoom.domain;

import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.NoteTag;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity(name = "Tag")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @OneToMany(mappedBy = "tag")
    private List<NoteTag> noteTagList = new ArrayList<>();
}
