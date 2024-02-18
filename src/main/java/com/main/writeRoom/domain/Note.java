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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "Note")
@Getter
@DynamicUpdate
@DynamicInsert
@Builder(toBuilder = true) // toBuilder 속성을 true로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Note extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault(" '제목을 입력하세요' ")
    private String title;

    @ColumnDefault(" '부제목을 입력하세요' ")
    private String subtitle;

    private String coverImg;

    @ColumnDefault(" '내용을 작성하는 곳' ")
    private String content;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("false")
    private ACHIEVE achieve; //노트 200자 달성 여부 true/false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @OneToMany(mappedBy = "note",cascade = CascadeType.ALL)
    private List<NoteTag> noteTagList = new ArrayList<>();

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<BookmarkNote> bookmarkNoteList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
