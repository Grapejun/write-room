package com.main.writeRoom.oauth;

import com.main.writeRoom.converter.UserConverter;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.enums.Role;
import com.main.writeRoom.oauth.domain.AuthTokensGenerator;
import com.main.writeRoom.oauth.domain.OAuthInfoResponse;
import com.main.writeRoom.oauth.domain.OAuthLoginParams;
import com.main.writeRoom.oauth.domain.RequestOAuthInfoService;
import com.main.writeRoom.oauth.infra.KakaoInfoResponse;
import com.main.writeRoom.oauth.infra.KakaoLoginParams;
import com.main.writeRoom.repository.UserRepository;
import com.main.writeRoom.web.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    // OAuth 로그인을 수행하고, 인증 토큰을 반환
    public AuthTokens login(OAuthLoginParams params) {
        // OAuth 인증 정보를 요청
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        // 멤버를 찾거나 새로 생성한 후 해당 멤버의 ID를 반환
        long memberId = findOrCreateMember(oAuthInfoResponse);
        User user = memberRepository.getReferenceById(memberId);
        UserResponseDTO.CustomUserInfo info = UserConverter.CustomUserInfoResultDTO(user);
        // 멤버 ID를 사용하여 인증 토큰을 생성해서 반환
        return authTokensGenerator.generate(info);
    }

    public UserResponseDTO.OauthLoginDTO login(KakaoLoginParams params) {
        // 카카오 인증 정보 요청
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        User user = memberRepository.getReferenceById(memberId);

        // CustomUserInfo 객체 생성
        UserResponseDTO.CustomUserInfo customUserInfo = UserResponseDTO.CustomUserInfo.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(Role.USER) // 역할 설정을 열거형 값으로 변경
                .build();

        // 토큰 생성
        AuthTokens tokens = authTokensGenerator.generate(customUserInfo);

        // OauthLoginDTO 객체 생성 및 반환
        return UserResponseDTO.OauthLoginDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(Role.USER.name()) // 'USER' 문자열 대신 Role 열거형의 name() 메서드를 사용
                .accessToken(tokens.getAccessToken())
                .build();
    }

    // 주어진 OAuth 정보를 사용하여 멤버를 찾거나 새로 생성
    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        User user = memberRepository.findByEmail(oAuthInfoResponse.getEmail());
        if (user != null) {
            return user.getId();
        } else {
            return newMember(oAuthInfoResponse);
        }
    }

        // 새 멤버를 생성하고 저장
    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        // OAuth 정보를 사용하여 멤버 객체를 생성
        User member = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();
        // 멤버 객체를 저장하고 저장된 객체의 ID를 반환
        return memberRepository.save(member).getId();
    }
}
