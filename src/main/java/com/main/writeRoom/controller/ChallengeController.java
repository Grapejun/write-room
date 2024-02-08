package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Challenge.ChallengeGoals;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.IsActive;
import com.main.writeRoom.repository.ChallengeGoalsParticipationRepository;
import com.main.writeRoom.repository.ChallengeRoutineParticipationRepository;
import com.main.writeRoom.service.ChallengeService.*;
import com.main.writeRoom.service.NoteService.NoteQueryService;
import com.main.writeRoom.service.RoomService.RoomQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.validation.annotation.PageLessNull;
import com.main.writeRoom.web.dto.challenge.ChallengeRequestDTO;
import com.main.writeRoom.web.dto.challenge.ChallengeResponseDTO;
import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.time.LocalDate;
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
    private final RoomQueryService roomQueryService;
    private final NoteQueryService noteQueryService;

    //챌린지 루틴 생성
    @PostMapping("/challenge-routines/create")
    @Operation(summary = "챌린지 루틴 생성 API", description = "루틴 만들기 화면에서 챌린지 루틴을 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4003", description = "챌린지 시작 날짜가 오늘부터여야 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4004", description = "챌린지 마감 날짜의 범위를 벗어났습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "roomId", description = "챌린지가 진행될 룸의 식별자를 입력하세요."),
    })
    public ApiResponse<ChallengeResponseDTO.CreateChallengeResultDTO> createChallengeRoutine(@RequestParam Long roomId, @Valid @RequestBody ChallengeRequestDTO.ChallengeRoutineDTO request) {
        ChallengeRoutine challengeRoutine = routineCommandService.create(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toCreateChallengeRoutineResultDTO(challengeRoutine));
    }

    //챌린지 루틴 조회(참여자 클릭 시 조회도 동일)
    @GetMapping("/challenge-routines/{challengeId}")
    @Operation(summary = "챌린지 루틴 조회 API", description = "루틴 만들기 챌린지 진행화면에서 챌린지 루틴을 조회하는 API입니다.(참여자 클릭 시의 챌린지 루틴 조회도 포함)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "진행 중인 챌린지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "challengeId", description = "조회할 챌린지 루틴의 식별자를 입력하세요.")
    })
    public ApiResponse<ChallengeResponseDTO.ChallengeRoutineDTO> getChallengeRoutine(@AuthUser long user, @PathVariable(name = "challengeId") Long challengeId) {
        User user1 = userQueryService.findUser(user);
        ChallengeRoutine routine = routineQueryService.findRoutine(challengeId);
        routineCommandService.isStatusProgress(user1, routine);
        List<ChallengeResponseDTO.NoteDTO> noteList = routineQueryService.findNoteDate(user1, routine);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toChallengeRoutineDTO(user1, routine, noteList));
    }

    //챌린지 루틴 달성 노트 조회
    @GetMapping("/challenge-routines/{roomId}/notes")
    @Operation(summary = "챌린지 루틴 달성 노트 조회 API", description = "루틴 만들기 챌린지 진행화면에서 챌린지 루틴 달성 스탬프를 눌렀을 때 해당 날짜에 작성된 200자 이상의 노트들이 조회되는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "roomId", description = "룸 아이디 입니다."),
            @Parameter(name = "page", description = "페이지 번호, 0번이 1번 페이지 입니다."),
            @Parameter(name="date", description = "스탬프에 쓰여진 챌린지 달성 날짜입니다.")
    })
    public ApiResponse<ChallengeResponseDTO.RoomResultByDate> getNoteListByDate(@AuthUser long user, @PathVariable(name = "roomId") Long roomId, @PageLessNull @RequestParam(name = "page") Integer page, @RequestParam LocalDate date) {
        Room room = roomQueryService.findRoom(roomId);
        Page<Note> note = noteQueryService.findNoteListForRoomAndUser(room, userQueryService.findUser(user), page);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toRoomResultByDate(room, note, date));
    }

    //챌린지 루틴 포기
    @PatchMapping("challenge-routines/give-up/{challengeId}")
    @Operation(summary = "챌린지 루틴 포기 API", description = "챌린지 포기하기 팝업에서 챌린지 루틴을 포기하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "진행 중인 챌린지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "challengeId", description = "포기할 챌린지 루틴의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    public ApiResponse giveUpChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        ChallengeRoutineParticipation routineParticipation = routineCommandService.giveUp(user, challengeId);
        return ApiResponse.onSuccess();
    }

    //챌린지 목표량
    //챌린지 목표량 생성
    @PostMapping("/challenge-goals/create")
    @Operation(summary = "챌린지 목표량 생성 API", description = "목표량 달성하기 화면에서 챌린지 목표량을 생성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4001", description = "룸이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4003", description = "챌린지 시작 날짜가 오늘부터여야 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ROOM4004", description = "챌린지 마감 날짜의 범위를 벗어났습니다.",
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
    @GetMapping("/challenge-goals/{challengeId}")
    @Operation(summary = "챌린지 목표량 조회 API", description = "목표량 달성하기 진행화면에서 챌린지 목표량을 조회하는 API입니다.(참여자 클릭 시의 챌린지 목표량 조회도 포함)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "진행 중인 챌린지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name = "challengeId", description = "조회할 챌린지 목표량의 식별자를 입력하세요.")
    })
    public ApiResponse<ChallengeResponseDTO.ChallengeGoalsDTO> getChallengeGoals(@AuthUser long user, @PathVariable(name = "challengeId") Long challengeId) {
        User user1 = userQueryService.findUser(user);
        ChallengeGoals goals = goalsQueryService.findGoals(challengeId);
        goalsCommandService.isStatusProgress(user1, goals);
        Integer achieveCount = goalsQueryService.findAchieveNote(user1, goals);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toChallengeGoalsDTO(user1, goals, achieveCount));
    }

    //챌린지 목표량 포기
    @PatchMapping("challenge-goals/give-up/{challengeId}")
    @Operation(summary = "챌린지 목표량 포기 API", description = "챌린지 포기하기 팝업에서 챌린지 목표량을 포기하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "챌린지 목표량이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "진행 중인 챌린지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "challengeId", description = "포기할 챌린지 목표량의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    public ApiResponse giveUpChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        ChallengeGoalsParticipation goalsParticipation = goalsCommandService.giveUP(user, challengeId);
        return ApiResponse.onSuccess();
    }

    //나의 챌린지
    //나의 챌린지 이력 목록 조회
    @GetMapping("/my-challenges/{roomId}")
    @Operation(summary = "나의 챌린지 목록 조회 API", description = "나의 챌린지 화면에서 나의 챌린지 목록을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4007", description = "룸과 회원에 해당하는 챌린지가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @Parameters({
            @Parameter(name = "user", description = "user", hidden = true),
            @Parameter(name="roomId", description = "현재 룸의 식별자를 입력하세요.")
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeListDTO> getMyChallengeList(@AuthUser long user, @PathVariable(name = "roomId") Long roomId) {
        List<ChallengeRoutineParticipation> routineParticipationList = myChallengeQueryService.findChallengeRoutineParticipation(user, roomId).stream()
                .filter(routineParticipation -> routineParticipation.getIsActive() == IsActive.ACTIVE).collect(Collectors.toList());
        List<ChallengeRoutine> routineList = myChallengeQueryService.findChallengeRoutine(routineParticipationList);
        List<ChallengeGoalsParticipation> goalsParticipationList = myChallengeQueryService.findChallengeGoalsParticipation(user, roomId).stream()
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
    @GetMapping("/my-challenges/challenge-routines/{challengeId}")
    @Operation(summary = "나의 챌린지 루틴 상세 조회 API", description = "나의 챌린지 > 챌린지 상세 화면에서 나의 챌린지 루틴을 조회하는 API입니다.")
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
            @Parameter(name = "challengeId", description = "나의 챌린지에서 상세 조회할 챌린지의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true),
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeRoutineDTO> getMyChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        User user1 = userQueryService.findUser(user);
        ChallengeRoutine routine = routineQueryService.findRoutine(challengeId);
        ChallengeRoutineParticipation routineParticipation = myChallengeQueryService.findByUserAndChallengeRoutine(user1, routine);
        List<ChallengeResponseDTO.NoteDTO> noteList = routineQueryService.findNoteDate(user1, routine);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toMyChallengeRoutineDTO(user1, routine, noteList, routineParticipation));
    }

    //나의 챌린지 상세 조회 - 목표량
    @GetMapping("/my-challenges/challenge-goals/{challengeId}")
    @Operation(summary = "나의 챌린지 목표량 상세 조회 API", description = "나의 챌린지 > 챌린지 상세 화면에서 나의 챌린지 목표량을 조회하는 API입니다.")
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
            @Parameter(name = "challengeId", description = "나의 챌린지에서 상세 조회할 챌린지의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true),
    })
    public ApiResponse<ChallengeResponseDTO.MyChallengeGoalsDTO> getMyChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        User user1 = userQueryService.findUser(user);
        ChallengeGoals goals = goalsQueryService.findGoals(challengeId);
        ChallengeGoalsParticipation goalsParticipation = myChallengeQueryService.findByUserAndChallengeGoals(user1, goals);
        Integer achieveCount = goalsQueryService.findAchieveNote(user1, goals);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toMyChallengeGoalsDTO(user1, goals, achieveCount, goalsParticipation));
    }

    //챌린지 내역 삭제 - 루틴
    @PatchMapping("/my-challenges/challenge-routines/delete/{challengeId}")
    @Operation(summary = "나의 챌린지 내역 삭제 API - 루틴", description = "나의 챌린지 > 챌린지 상세> 챌린지 삭제 확인 팝업에서 나의 챌린지(루틴) 내역을 삭제하는 API입니다.")
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
            @Parameter(name = "challengeId", description = "나의 챌린지 내역에서 삭제할 챌린지 루틴의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    public ApiResponse deleteChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        ChallengeRoutineParticipation routineParticipation = myChallengeQueryService.findByUserAndChallengeRoutine(userQueryService.findUser(user), routineQueryService.findRoutine(challengeId));
        myChallengeCommandService.inactiveRoutine(routineParticipation);
        return ApiResponse.onSuccess();
    }

    //나의 챌린지 내역 삭제 - 목표량
    @PatchMapping("/my-challenges/challenge-goals/delete/{challengeId}")
    @Operation(summary = "나의 챌린지 내역 삭제 API - 목표량", description = "나의 챌린지 > 챌린지 상세> 챌린지 삭제 확인 팝업에서 나의 챌린지(목표량) 내역을 삭제하는 API입니다.")
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
            @Parameter(name = "challengeId", description = "나의 챌린지 내역에서 삭제할 챌린지 목표량의 식별자를 입력하세요."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    public ApiResponse deleteChallengeGoals(@PathVariable(name = "challengeId") Long challengeId, @AuthUser long user) {
        ChallengeGoalsParticipation goalsParticipation = myChallengeQueryService.findByUserAndChallengeGoals(userQueryService.findUser(user), goalsQueryService.findGoals(challengeId));
        myChallengeCommandService.inactiveGoals(goalsParticipation);
        return ApiResponse.onSuccess();
    }
}
