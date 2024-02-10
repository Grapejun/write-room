package com.main.writeRoom.domain.User;

import com.main.writeRoom.common.BaseEntity;
import com.main.writeRoom.domain.enums.Role;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.oauth.domain.OAuthProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private JoinType joinType;
    private String profileImage;
    private OAuthProvider oAuthProvider;
    private String resetToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<RoomParticipation> roomParticipationList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExistedEmail> existedEmails = new ArrayList<>();

    public User setProfile(String nickName, String userImg) {
        this.name = nickName != null ? nickName : name;
        this.profileImage = userImg != null ? userImg : profileImage;
        return this;
    }
    public User setPassword(String updatedPwd) {
        this.password = updatedPwd;
        return this;
    }
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}