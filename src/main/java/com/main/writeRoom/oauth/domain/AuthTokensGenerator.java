package com.main.writeRoom.oauth.domain;

import com.main.writeRoom.config.utils.JwtUtil;
import com.main.writeRoom.oauth.AuthTokens;
import com.main.writeRoom.oauth.provider.JwtTokenProvider;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer"; // 토큰 타입을 "Bearer"로 설정
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;    // AccessToken 14일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // RefreshToken 14일

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    // 사용자 식별자
    public AuthTokens generate(UserResponseDTO.CustomUserInfo response) {
        long now = (new Date()).getTime();

        // 각 토큰의 만료 시간을 계산
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // 멤버 ID를 JWT의 subject로 사용
        String subject = response.getUserId().toString();
        String accessTokenJWT = jwtUtil.createAccessToken(response);
        // 각 토큰(accessToken, refreshToken) 을 생성
        //String accessTokenJWT = jwtTokenProvider.generate(subject, accessTokenExpiredAt);

        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);
        // 생성된 토큰들과 추가 정보를 함께 반환
        return AuthTokens.of(accessTokenJWT, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    // 주어진 접근 토큰에서 사용자 ID를 추출
    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}
