package com.main.writeRoom.converter;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeParticipationConverter {

    public static List<ChallengeRoutineParticipation> toChallengeRoutineParticipation(List<User> userList) {
        return userList.stream()
                .map(user ->
                        ChallengeRoutineParticipation.builder()
                                .user(user)
                                .challengeStatus(ChallengeStatus.PROGRESS)
                                .build()
                ).collect(Collectors.toList());
    }
}
