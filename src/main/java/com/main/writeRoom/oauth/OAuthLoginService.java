package com.main.writeRoom.oauth;

import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.oauth.domain.AuthTokensGenerator;
import com.main.writeRoom.oauth.domain.OAuthInfoResponse;
import com.main.writeRoom.oauth.domain.OAuthLoginParams;
import com.main.writeRoom.oauth.domain.RequestOAuthInfoService;
import com.main.writeRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        // 멤버 ID를 사용하여 인증 토큰을 생성해서 반환
        return authTokensGenerator.generate(memberId);
    }

    // 주어진 OAuth 정보를 사용하여 멤버를 찾거나 새로 생성
    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        // 이메일을 기반으로 멤버를 find

        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
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
