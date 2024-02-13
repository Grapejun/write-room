package com.main.writeRoom.domain;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Emoji")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Emoji extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Long emojiNum;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}
