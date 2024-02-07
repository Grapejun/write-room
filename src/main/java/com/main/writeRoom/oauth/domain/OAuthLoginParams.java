package com.main.writeRoom.oauth.domain;

import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();

}
