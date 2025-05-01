package com.kwakmunsu.diary.auth.controller;

import static com.kwakmunsu.diary.global.jwt.common.TokenType.REFRESH;

import com.kwakmunsu.diary.auth.controller.dto.MemberCreateRequest;
import com.kwakmunsu.diary.auth.controller.dto.MemberLoginRequest;
import com.kwakmunsu.diary.auth.service.AuthCommandService;
import com.kwakmunsu.diary.auth.service.AuthQueryService;
import com.kwakmunsu.diary.global.jwt.dto.TokenResponse;
import com.kwakmunsu.diary.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;


    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        Long memberId = authCommandService.signUp(request.toServiceRequest());
        URI location = URI.create("/members/" + memberId);

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            HttpServletResponse response,
            @Valid @RequestBody MemberLoginRequest request
    ) {
        TokenResponse tokenResponse = authQueryService.login(request.toServiceRequest());
        // 같은 이름이 있다면 기존에 있던 쿠키 덮어짐.
        addCookie(response, tokenResponse);

        return ResponseEntity.ok(tokenResponse.accessToken());
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(
            HttpServletResponse response,
            @CookieValue("refreshToken") final String refreshToken
    ) {
        TokenResponse tokenResponse = authQueryService.reissue(refreshToken);
        // 같은 이름이 있다면 기존에 있던 쿠키 덮어짐.
        addCookie(response, tokenResponse);

        return ResponseEntity.ok(tokenResponse.accessToken());
    }

    private void addCookie(HttpServletResponse response, TokenResponse memberTokens) {
        Cookie cookie = CookieUtil.create(REFRESH.getValue(), memberTokens.refreshToken());
        response.addCookie(cookie);
    }

}