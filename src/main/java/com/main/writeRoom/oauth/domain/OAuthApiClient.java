package com.main.writeRoom.oauth.domain;

public interface OAuthApiClient {
    // OAuth 제공자(KaKao, Google) 정보 반환
    OAuthProvider oAuthProvider();
    // OAuth 제공자에게 토큰 요청 (OAuthLoginParams 로 받아옴)
    String requestAccessToken(OAuthLoginParams params);
    // 주어진 Access Token 으로 OAuth 제공자에게 사용자 정보 요청
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
