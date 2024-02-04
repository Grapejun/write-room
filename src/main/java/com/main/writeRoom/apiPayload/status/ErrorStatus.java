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
    // 유저 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4002", "이메일이 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER4003", "비밀번호가 일치하지 않습니다."),

    PAGE_LESS_NULL(HttpStatus.BAD_REQUEST, "PAGE4001", "Page는 0부터 입니다."),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROOM4001", "룸이 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4001", "카테고리가 없습니다."),
    NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTE4001", "노트가 없습니다."),
    AUTHORITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTHORITY4001", "권한이 없습니다."),
    AUTHORITY_TYPE_ERROR(HttpStatus.BAD_REQUEST, "AUTHORITY4002", "올바른 권한 형식을 입력하세요."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOOKMARK4001", "해당하는 Id의 북마크가 없습니다."),

    //챌린지 에러
    ROUTINE_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4001", "챌린지 루틴이 없습니다."),
    PROGRESS_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4002", "진행 중인 챌린지가 없습니다."),
    STARTDATE_NOT_TODAY(HttpStatus.BAD_REQUEST, "CHALLENGE4003", "챌린지 시작 날짜가 오늘부터여야 합니다."),
    DEADLINE_OUT_RANGE(HttpStatus.BAD_REQUEST, "CHALLENGE4004", "챌린지 마감 날짜 범위를 벗어났습니다."),
    GOALS_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4005", "챌린지 목표량이 없습니다."),
    NOT_PARTICIPATE(HttpStatus.BAD_REQUEST, "CHALLENGE4006", "해당 챌린지에 사용자가 참여하지 않았습니다."),
    CHALLENGE_NOTFOUND(HttpStatus.BAD_REQUEST, "CHALLENGE4007", "룸과 회원에 해당하는 챌린지가 없습니다.")
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
