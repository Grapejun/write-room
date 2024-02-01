package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.IsActive;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.service.ChallengeService.*;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeRoutineCommandService routineCommandService;
    private final ChallengeRoutineQueryService routineQueryService;
    private final UserQueryService userQueryService;
    private final ChallengeGoalsCommandService goalsCommandService;
    private final ChallengeGoalsQueryService goalsQueryService;
    private final MyChallengeQueryService myChallengeQueryService;
    private final ChallengeRoutineParticipationRepository routineParticipationRepository;
    private final ChallengeGoalsParticipationRepository goalsParticipationRepository;
    private final MyChallengeCommandService myChallengeCommandService;

    //1. 챌린지 루틴 생성
    @PostMapping("/challenge-routines/create")
    @Operation(summary = "챌린지 루틴 생성 API", description = "챌린지 루틴을 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "챌린지가 진행될 룸의 식별자를 입력하세요."),
    })
    public ApiResponse<ChallengeResponseDTO.CreateChallengeResultDTO> createChallengeRoutine(@RequestParam Long roomId, @Valid @RequestBody ChallengeRequestDTO.ChallengeRoutineDTO request) {
        ChallengeRoutine challengeRoutine = routineCommandService.create(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toCreateChallengeRoutineResultDTO(challengeRoutine));
    }

    //2. 챌린지 루틴 조회(참여자 클릭 시 조회도 동일)
    @GetMapping("/challenge-routines/{userId}/{challengeId}")
    @Operation(summary = "챌린지 루틴 조회 API", description = "챌린지 루틴을 조회하는 API입니다.(참여자 클릭 시의 챌린지 루틴 조회도 포함)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4004", description = "챌린지 마감 날짜 범위를 벗어났습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters
    public ApiResponse<ChallengeResponseDTO.ChallengeRoutineDTO> getChallengeRoutine(@PathVariable(name = "userId") Long userId, @PathVariable(name = "challengeId") Long challengeId) {
        User user = userQueryService.findUser(userId);
        ChallengeRoutine routine = routineQueryService.findRoutine(challengeId);
        List<ChallengeResponseDTO.NoteDTO> noteList = routineQueryService.findNoteDate(user, routine);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toChallengeRoutineDTO(user, routine, noteList));
    }

    //3. 챌린지 루틴 조회 - 스탬프 클릭 -> 노트 조회
    @GetMapping("/challenge-routines/{userId}/{roomId}/notes")
    @Operation(summary = "챌린지 루틴 달성 노트 조회 API", description = "챌린지 루틴 달성 스탬프를 눌렀을 때 해당 날짜에 작성된 200자 이상의 노트들이 조회되는 API입니다.")
    @ApiResponses
    @Parameters
    public ApiResponse<ChallengeResponseDTO.NoteListByDateDTO> getNoteListByDate(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roodId") Long roomId, @RequestParam List<Long> noteId) {
        return null;
    }

    //5. 챌린지 루틴 포기
    @PatchMapping("challenge-routines/give-up/{challengeId}")
    @Operation(summary = "챌린지 루틴 포기 API", description = "챌린지 루틴을 포기하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "회원이 해당 챌린지에 참여하지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "챌린지를 포기할 회원의 식별자를 입력하세요."),
    })
    public ApiResponse giveUpChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        ChallengeRoutineParticipation routineParticipation = routineCommandService.giveUp(userId, challengeId);
        return ApiResponse.onSuccess();
    }

    //챌린지 목표량
    //챌린지 목표량 생성
    @PostMapping("/challenge-goals/create")
    @Operation(summary = "챌린지 목표량 생성 API", description = "챌린지 목표량을 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "roomId", description = "챌린지가 진행될 룸의 식별자를 입력하세요."),
    })
    public ApiResponse<ChallengeResponseDTO.CreateChallengeResultDTO> createChallengeGoals(@RequestParam Long roomId, @Valid @RequestBody ChallengeRequestDTO.ChallengeGoalsDTO request) {
        ChallengeGoals challengeGoals = goalsCommandService.create(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toCreateChallengeGoalsResultDTO(challengeGoals));
    }

    //챌린지 목표량 조회
    @GetMapping("/challenge-goals/{userId}/{challengeId}")
    @Operation(summary = "챌린지 목표량 조회 API", description = "챌린지 목표량을 조회하는 API입니다.(참여자 클릭 시의 챌린지 목표량 조회도 포함)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4004", description = "챌린지 마감 날짜 범위를 벗어났습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters
    public ApiResponse<ChallengeResponseDTO.ChallengeGoalsDTO> getChallengeGoals(@PathVariable(name = "userId") Long userId, @PathVariable(name = "challengeId") Long challengeId) {
        User user = userQueryService.findUser(userId);
        ChallengeGoals goals = goalsQueryService.findGoals(challengeId);
        Integer achieveCount = goalsQueryService.findAchieveNote(user, goals);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toChallengeGoalsDTO(user, goals, achieveCount));
    }

    //챌린지 목표량 포기
    @PatchMapping("challenge-goals/give-up/{challengeId}")
    @Operation(summary = "챌린지 목표량 포기 API", description = "챌린지 목표량을 포기하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "회원이 해당 챌린지에 참여하지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "챌린지를 포기할 회원의 식별자를 입력하세요."),
    })
    public ApiResponse giveUpChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        ChallengeGoalsParticipation goalsParticipation = goalsCommandService.giveUP(userId, challengeId);
        return ApiResponse.onSuccess();
    }

    //나의 챌린지
    //나의 챌린지 이력 목록 조회
    @GetMapping("/my-challenges/{userId}/{roomId}")
    @Operation(summary = "나의 챌린지 목록 조회 API", description = "나의 챌린지 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "나의 챌린지를 조회할 회원의 식별자를 입력하세요."),
            @Parameter(name="roomId", description = "현재 룸의 식별자를 입력하세요.")
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeListDTO> getMyChallengeList(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roomId") Long roomId) {
        List<ChallengeRoutineParticipation> routineParticipationList = myChallengeQueryService.findChallengeRoutineParticipation(userId, roomId).stream()
                .filter(routineParticipation -> routineParticipation.getIsActive() == IsActive.ACTIVE).collect(Collectors.toList());
        List<ChallengeRoutine> routineList = myChallengeQueryService.findChallengeRoutine(routineParticipationList);
        List<ChallengeGoalsParticipation> goalsParticipationList = myChallengeQueryService.findChallengeGoalsParticipation(userId, roomId).stream()
                .filter(goalsParticipation -> goalsParticipation.getIsActive() == IsActive.ACTIVE).collect(Collectors.toList());
        List<ChallengeGoals> goalsList = myChallengeQueryService.findChallengeGoals(goalsParticipationList);

        List<ChallengeResponseDTO.MyChallengeDTO> myRoutineList = new ArrayList<>();
        for (int i = 0; i < routineList.size(); i++) {
            myRoutineList.add(ChallengeConverter.toMyChallengeDTO(routineList.get(i), routineParticipationList.get(i)));
        }

        List<ChallengeResponseDTO.MyChallengeDTO> myGoalsList = new ArrayList<>();
        for (int i = 0; i < goalsList.size(); i++) {
            myGoalsList.add(ChallengeConverter.toMyChallengeDTO(goalsList.get(i), goalsParticipationList.get(i)));
        }

        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toMyChallengeDTOList(myRoutineList, myGoalsList));
    }

    //나의 챌린지 상세 조회 - 루틴
    @GetMapping("/my-challenge/challenge-routines/{challengeId}")
    @Operation(summary = "나의 챌린지 루틴 상세 조회 API", description = "나의 챌린지 루틴을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "challengeId", description = "나의 챌린지에서 상세 조회할 챌린지의 식별자를 입력하세요."),
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeRoutineDTO> getMyChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        User user = userQueryService.findUser(userId);
        ChallengeRoutine routine = routineQueryService.findRoutine(challengeId);
        ChallengeRoutineParticipation routineParticipation = routineParticipationRepository.findByUserAndChallengeRoutine(user, routine);
        List<ChallengeResponseDTO.NoteDTO> noteList = routineQueryService.findNoteDate(user, routine);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toMyChallengeRoutineDTO(user, routine, noteList, routineParticipation));
    }

    //나의 챌린지 상세 조회 - 목표량
    @GetMapping("/my-challenges/challenge-goals/{challengeId}")
    @Operation(summary = "나의 챌린지 목표량 상세 조회 API", description = "나의 챌린지 목표량을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "challengeId", description = "나의 챌린지에서 상세 조회할 챌린지의 식별자를 입력하세요."),
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeGoalsDTO> getMyChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        User user = userQueryService.findUser(userId);
        ChallengeGoals goals = goalsQueryService.findGoals(challengeId);
        ChallengeGoalsParticipation goalsParticipation = goalsParticipationRepository.findByUserAndChallengeGoals(user, goals);
        Integer achieveCount = goalsQueryService.findAchieveNote(user, goals);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toMyChallengeGoalsDTO(user, goals, achieveCount, goalsParticipation));
    }

    //챌린지 내역 삭제 - 루틴
    @PatchMapping("/my-challenges/challenge-routines/delete/{challengeId}")
    @Operation(summary = "나의 챌린지 내역 삭제 API - 루틴", description = "나의 챌린지(루틴) 내역을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "회원이 해당 챌린지에 참여하지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원의 식별자를 입력하세요."),
    })
    public ApiResponse deleteChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        ChallengeRoutineParticipation routineParticipation = routineParticipationRepository.findByUserAndChallengeRoutine(userQueryService.findUser(userId), routineQueryService.findRoutine(challengeId));
        myChallengeCommandService.inactiveRoutine(routineParticipation);
        return ApiResponse.onSuccess();
    }

    //나의 챌린지 내역 삭제 - 목표량
    @PatchMapping("/my-challenges/challenge-goals/delete/{challengeId}")
    @Operation(summary = "나의 챌린지 내역 삭제 API - 목표량", description = "나의 챌린지(목표량) 내역을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "회원이 해당 챌린지에 참여하지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원의 식별자를 입력하세요."),
    })
    public ApiResponse deleteChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        ChallengeGoalsParticipation goalsParticipation = goalsParticipationRepository.findByUserAndChallengeGoals(userQueryService.findUser(userId), goalsQueryService.findGoals(challengeId));
        myChallengeCommandService.inactiveGoals(goalsParticipation);
        return ApiResponse.onSuccess();
    }
}
