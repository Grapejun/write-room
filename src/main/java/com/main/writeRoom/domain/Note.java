package com.main.writeRoom.domain;

import com.main.writeRoom.domain.Bookmark.BookmarkNote;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.common.BaseEntity;
import com.main.writeRoom.domain.mapping.NoteTag;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Note")
@Getter
@Builder(toBuilder = true) // toBuilder 속성을 true로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Note extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subtitle;
    private String coverImg;
    private String content;
    @Enumerated(EnumType.STRING)
    private ACHIEVE achieve; //노트 200자 달성 여부 true/false

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @OneToMany(mappedBy = "note",cascade = CascadeType.ALL)
    private List<NoteTag> noteTagList = new ArrayList<>();

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<BookmarkNote> bookmarkNoteList = new ArrayList<>();

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
}
