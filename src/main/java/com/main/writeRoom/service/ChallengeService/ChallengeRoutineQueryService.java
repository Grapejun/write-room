package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeRoutineQueryService {

    public ChallengeRoutine findRoutine(Long challengeId);
    public List<ChallengeResponseDTO.NoteDTO> findNoteDate(User user, ChallengeRoutine challengeRoutine);
    public ChallengeRoutineParticipation findRoutineParticipation(User user, ChallengeRoutine routine);

    public ChallengeRoutineParticipation findProgressRoutineParticipation(User user, Room room);
}
