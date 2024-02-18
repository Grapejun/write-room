package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.handler.ChallengeHandler;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeGoalsRepository;
import com.main.writeRoom.repository.NoteRepository;
import com.main.writeRoom.service.RoomParticipationService.RoomParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeGoalsQueryServiceImpl implements ChallengeGoalsQueryService{
    private final ChallengeGoalsRepository goalsRepository;
    private final NoteRepository noteRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;
    private final RoomParticipationService roomParticipationService;

    @Override
    public ChallengeGoals findGoals(Long challengeId) {
        return goalsRepository.findById(challengeId).orElseThrow(()-> new ChallengeHandler(ErrorStatus.GOALS_NOTFOUND));
    }

    @Override
    public ChallengeGoalsParticipation findProgressGoalsParticipation(User user, Room room) {
        return goalsParticipationRepository.findProgressGoalsParticipation(user, room);
    }

    @Override
    public List<ChallengeGoalsParticipation> findByChallengeStatus(ChallengeStatus challengeStatus) {
        return goalsParticipationRepository.findByChallengeStatus(challengeStatus);
    }

    @Override
    public List<User> findGoalsUsers(Room room) {
        List<User> userList = roomParticipationService.findRoomUserList(room).stream()
                .filter(user -> findProgressGoalsParticipation(user, room) == null) //진행 중인 참여자를 조회했을 때 null이 반환되는 참여자로 필터링
                .collect(Collectors.toList());
        return userList;
    }

}
