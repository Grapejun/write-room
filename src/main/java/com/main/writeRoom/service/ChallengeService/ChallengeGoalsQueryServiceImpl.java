package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.handler.ChallengeHandler;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeGoalsRepository;
import com.main.writeRoom.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeGoalsQueryServiceImpl implements ChallengeGoalsQueryService{
    private final ChallengeGoalsRepository goalsRepository;
    private final NoteRepository noteRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;

    @Override
    public ChallengeGoals findGoals(Long challengeId) {
        return goalsRepository.findById(challengeId).orElseThrow(()-> new ChallengeHandler(ErrorStatus.GOALS_NOTFOUND));
    }

    @Override
    public Integer findAchieveNote(User user, ChallengeGoals goals) {
        if (goals.getStartDate() == null) {
            return noteRepository.findAchieveCountNoDate(user, goals.getRoom());
        }
        else return noteRepository.findAchieveCount(goals.getStartDate().atStartOfDay(), goals.getDeadline().atTime(LocalTime.MAX), user,goals.getRoom());
    }

    @Override
    public ChallengeGoalsParticipation findGoalsParticipation(User user, ChallengeGoals goals) {
        return goalsParticipationRepository.findByUserAndChallengeGoals(user, goals);
    }
}
