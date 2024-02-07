package com.main.writeRoom.oauth;

import com.main.writeRoom.oauth.infra.KakaoLoginParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController2 {

    private final OAuthLoginService oAuthLoginService;


//     POST 요청 "/api/auth/kakao" 매핑
//    @Operation(summary = "카카오 인가코드 이용하여 토큰 발급받기", description = """
//            카카오 인가코드를 request body에 담아 전달해주세요.
//
//            response 값에 들어있는 acccessToken을 저장했다가 앞으로 서버에 api요청시 사용해주세요.
//
//            후에 accessToken 사용시 request header에 Authorization값에 "Bearer 전달받은엑세스토큰" 형식으로 사용해주세요.
//            """)
//    @PostMapping("/kakao")
//    @CrossOrigin(origins = "*",maxAge = 3600)
//    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) { // Athorization Code를 파라미터로 받음
//        // oAuthLoginService의 login 메서드를 호출, 인증 토큰을 받아서 반환
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }

        @Operation(summary = "카카오 인가코드 이용하여 토큰 발급받기", description = """
            카카오 인가코드를 request body에 담아 전달해주세요.

            response 값에 들어있는 acccessToken을 저장했다가 앞으로 서버에 api요청시 사용해주세요.

            후에 accessToken 사용시 request header에 Authorization값에 "Bearer 전달받은엑세스토큰" 형식으로 사용해주세요.
            """)
        @PostMapping("/kakao")
        @CrossOrigin(origins = "*", maxAge = 3600)
        public ResponseEntity<AuthTokens> loginKakao(@RequestParam("authCode") String authCode) { // 인가 코드를 query string 파라미터로 받음
            // oAuthLoginService의 login 메서드를 호출, 인증 토큰을 받아서 반환
            KakaoLoginParams params = new KakaoLoginParams(authCode); // 이 부분은 KakaoLoginParams 클래스가 authCode를 파라미터로 받는 생성자를 가지고 있다고 가정
            return ResponseEntity.ok(oAuthLoginService.login(params));

        }





}
