package com.main.writeRoom.service.ChallengeService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.handler.ChallengeHandler;
import com.main.writeRoom.repository.*;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeRoutineQueryServiceImpl implements ChallengeRoutineQueryService { //GET요청에 대한 로직

    private final ChallengeRoutineRepository routineRepository;
    private final NoteRepository noteRepository;
    private final UserQueryService userQueryService;
    private final ChallengeRoutineParticipationRepository routineParticipationRepository;

    @Override
    public ChallengeRoutine findRoutine(Long challengeId) {
        return routineRepository.findById(challengeId).orElseThrow(() -> new ChallengeHandler(ErrorStatus.ROUTINE_NOTFOUND));
    }

    @Override
    public List<ChallengeResponseDTO.NoteDTO> findNoteDate(User user, ChallengeRoutine routine) { //챌린지 루틴 기간 동안에 '200자 이상' 작성된 노트의 작성 날짜를 조회
        Room room = routine.getRoom();
        List<Note> noteList = noteRepository.findAchieveNotes(routine.getStartDate().atStartOfDay(), routine.getDeadline().atTime(LocalTime.MAX), user, room);
        List<ChallengeResponseDTO.NoteDTO> noteDTOList = noteList.stream()
                .map(note -> {
                    return ChallengeConverter.toNoteDTO(note);
                }).collect(Collectors.toList());
        return noteDTOList;
    }

    @Override
    public ChallengeRoutineParticipation findRoutineParticipation(User user, ChallengeRoutine routine) {
        return routineParticipationRepository.findByUserAndChallengeRoutine(user, routine);
    }
}
