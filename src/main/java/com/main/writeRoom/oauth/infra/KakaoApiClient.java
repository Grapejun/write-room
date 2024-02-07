package com.main.writeRoom.oauth.infra;

import com.main.writeRoom.oauth.domain.OAuthApiClient;
import com.main.writeRoom.oauth.domain.OAuthInfoResponse;
import com.main.writeRoom.oauth.domain.OAuthLoginParams;
import com.main.writeRoom.oauth.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}") // application.yml 값들 주입
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate; // 외부 HTTP 서비스와 통신하기 위함

    @Override
    public OAuthProvider oAuthProvider() {  // OAuth Provider <- KAKAO
        return OAuthProvider.KAKAO;
    }


    @Override   // AccessToken 요청하는 메서드
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";
        // 요청 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 요청 바디 설정
        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        // 요청 객체 생성
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        // 실제 외부  HTTP 요청 수행 및 응답 수신
        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);

        assert response != null;
        return response.getAccessToken();
    }
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me"; // 사용자 정보를 요청할 URL

        // HTTP 요청 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        // HttpEntity 객체 생성 (여기서는 본문이 필요 없으므로, 헤더만 포함)
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        // RestTemplate을 사용하여 GET 요청 수행 및 응답 수신
        ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, request, KakaoInfoResponse.class);

        return response.getBody();
    }
}
