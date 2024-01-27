package com.main.writeRoom.apiPayload.status;

import com.main.writeRoom.apiPayload.code.BaseErrorCode;
import com.main.writeRoom.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    PAGE_LESS_NULL(HttpStatus.BAD_REQUEST, "PAGE4001", "Page는 0부터 입니다."),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM4001", "룸이 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4001", "카테고리가 없습니다."),
    NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTE4001", "노트가 없습니다."),
    AUTHORITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTHORITY4001", "권한이 없습니다."),
    AUTHORITY_TYPE_ERROR(HttpStatus.BAD_REQUEST, "AUTHORITY4002", "올바른 권한 형식을 입력하세요."),

    //챌린지 에러
    ROUTINE_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4001", "챌린지 루틴이 없습니다."),
    PROGRESS_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4002", "진행 중인 챌린지가 없습니다."),
    STARTDATE_NOT_TODAY(HttpStatus.BAD_REQUEST, "CHALLENGE4003", "챌린지 시작 날짜가 오늘부터여야 합니다."),
    DEADLINE_OUT_RANGE(HttpStatus.BAD_REQUEST, "CHALLENGE4004", "챌린지 마감 날짜 범위를 벗어났습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isFailure(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isFailure(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
