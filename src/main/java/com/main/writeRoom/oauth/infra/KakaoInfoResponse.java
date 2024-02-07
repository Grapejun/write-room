package com.main.writeRoom.oauth.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.main.writeRoom.oauth.domain.OAuthInfoResponse;
import com.main.writeRoom.oauth.domain.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

//    @Override
//    public String getEmail() {
//        if (kakaoAccount != null) {
//            return kakaoAccount.email;
//        }
//        return null; // 또는 적절한 기본값 혹은 예외 처리
//    }


    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

//    @Override
//    public String getNickname() {
//        if (kakaoAccount != null && kakaoAccount.profile != null) {
//            return kakaoAccount.profile.nickname;
//        }
//        return null; // 또는 적절한 기본값 혹은 예외 처리
//    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
