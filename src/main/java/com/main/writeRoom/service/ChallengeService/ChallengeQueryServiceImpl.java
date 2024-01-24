package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.repository.ChallengeRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService{ //GET요청에 대한 로직

    private final ChallengeRoutineRepository challengeRoutineRepository;

    @Override
    public ChallengeRoutine read(Long userId) {
        //챌린지 루틴 참여 테이블에서 userId와 status==진행중인 챌린지 루틴 식별자를 얻는다.
        // Long challengeRoutineId =

        //얻은 챌린지 식별자로 조회.
        //ChallengeRoutine challengeRoutine = challengeRoutineRepository.findById();

        //return challengeRoutine;
        return null;
    }

}
