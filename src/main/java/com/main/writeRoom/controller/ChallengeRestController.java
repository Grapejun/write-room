package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.service.ChallengeService.ChallengeCommandService;
import com.main.writeRoom.service.ChallengeService.ChallengeQueryService;
import com.main.writeRoom.service.UserService.UserQueryService;
import com.main.writeRoom.service.UserService.UserQueryServiceImpl;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.time.LocalDate;
import java.util.List;

import static com.amazonaws.services.ec2.model.PrincipalType.User;

@RestController
@RequiredArgsConstructor
public class ChallengeRestController {

    private final ChallengeCommandService challengeCommandService;
    private final ChallengeQueryService challengeQueryService;
    private final UserQueryService userQueryService;

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
    public ApiResponse<ChallengeResponseDTO.CreateChallengeRoutineResultDTO> createChallengeRoutine(@RequestParam Long roomId, @Valid @RequestBody ChallengeRequestDTO.ChallengeRoutineDTO request) {
        ChallengeRoutine challengeRoutine = challengeCommandService.create(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toCreateChallengeRoutineResultDTO(challengeRoutine));
    }

    //2. 챌린지 루틴 조회
    @GetMapping("/challenge-routines/{userId}/{challengeId}")
    @Operation(summary = "챌린지 루틴 조회 API", description = "챌린지 루틴을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "챌린지 루틴이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters
    public ApiResponse<ChallengeResponseDTO.ChallengeRoutineDTO> readChallengeRoutine(@PathVariable(name = "userId") Long userId, @PathVariable(name = "challengeId") Long challengeId) {
        ChallengeRoutine challengeRoutine = challengeQueryService.findRoutine(challengeId);
        List<LocalDate> dateList = challengeQueryService.findNoteDate(userId, challengeId);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toChallengeRoutineDTO(userQueryService.findUser(userId), challengeRoutine, dateList));
    }

    //3. 챌린지 루틴 조회 - 스탬프 클릭 -> 노트 조회
    //4. 챌린지 루틴 조회 - 참여자 클릭

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
    public ApiResponse<ChallengeResponseDTO.GiveUpChallengeRoutineResultDTO> giveUpChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId, @RequestParam Long userId) {
        ChallengeRoutineParticipation routineParticipation = challengeCommandService.giveUP(userId, challengeId);
        return ApiResponse.of(SuccessStatus._OK, ChallengeConverter.toGiveUpChallengeRoutineResultDTO(routineParticipation));
    }
}
