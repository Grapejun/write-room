package com.main.writeRoom.oauth.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    // 생성자를 통해 여러 OAuthApiClient 객체를 주입
    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    //OAuth 로그인 요청 정보를 받아와 해당 요청에 맞는 OAuth 정보를 반환
    public OAuthInfoResponse request(OAuthLoginParams params) {
        // 해당 제공자의 클라이언트 객체를 가져옵니다.
        OAuthApiClient client = clients.get(params.oAuthProvider());
        // 클라이언트 객체로 액세스토큰 요청
        String accessToken = client.requestAccessToken(params);
        // 받아온 엑세스 토큰으로 OAuth정보 요청 및 반환
        return client.requestOauthInfo(accessToken);
    }
}
