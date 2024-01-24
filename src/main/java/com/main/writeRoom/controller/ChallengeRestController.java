package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import com.main.writeRoom.converter.ChallengeConverter;
import com.main.writeRoom.domain.Challenge.ChallengeRoutine;
import com.main.writeRoom.service.ChallengeService.ChallengeCommandService;
import com.main.writeRoom.service.ChallengeService.ChallengeQueryService;
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

@RestController
@RequiredArgsConstructor
public class ChallengeRestController {

    private final ChallengeCommandService challengeCommandService;
    private final ChallengeQueryService challengeQueryService;

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
        return ApiResponse.onSuccess(ChallengeConverter.toCreateChallengeRoutineResultDTO(challengeRoutine));
    }

    //2. 챌린지 루틴 조회
    @GetMapping("/challenge-routines/{userId}")
    @Operation(summary = "챌린지 루틴 조회 API", description = "챌린지 루틴을 조회하는 API입니다.")
    @ApiResponses
    @Parameters
    public ApiResponse<ChallengeResponseDTO.ChallengeRoutineDTO> readChallengeRoutine(@PathVariable(name = "userId") Long userId) {
        ChallengeRoutine challengeRoutine = challengeQueryService.read(userId);
        return ApiResponse.onSuccess(ChallengeConverter.toChallengeRoutineDTO(null, challengeRoutine));
    }

    //3. 챌린지 루틴 조회 - 스탬프 클릭
    //4. 챌린지 루틴 조회 - 참여자 클릭

    //5. 챌린지 루틴 포기
    @PatchMapping("challenge-routines/give-up/{challengeId}")
    @Operation(summary = "챌린지 루틴 포기 API", description = "챌린지 루틴을 포기하는 API입니다.")
    @ApiResponses
    @Parameters
    public ApiResponse<ChallengeResponseDTO.GiveUpChallengeRoutineResultDTO> giveUpChallengeRoutine(@PathVariable(name = "challengeId") Long challengeId) {
        return null;
    }
}
