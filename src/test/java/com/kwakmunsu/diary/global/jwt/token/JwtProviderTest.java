package com.kwakmunsu.diary.global.jwt.token;

import static org.assertj.core.api.Assertions.assertThat;

import com.kwakmunsu.diary.global.jwt.dto.TokenResponse;
import com.kwakmunsu.diary.member.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    private Long memberId;
    private Role role;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider("1233asdasdqweqw3123123asdffsdfasdfasggdd");
        this.memberId = 1L;
        this.role = Role.MEMBER;
    }
    @DisplayName("로그인 성공 후 AT와 RT를 반환한다")
    @Test
    void returnAccessTokenWhenLoginSuccess() {

        // when
        TokenResponse tokenResponse = jwtProvider.createTokens(memberId, role);
        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isNotBlank();
        assertThat(tokenResponse.refreshToken()).isNotBlank();
    }

    @DisplayName("엑세스 토큰으로 부터 Authentication 객체를 가져온다")
    @Test
    void getAuthenticationToAccessToken() {
        // given
        TokenResponse tokenResponse = jwtProvider.createTokens(memberId, role);
        // when
        Authentication authentication = jwtProvider.getAuthentication(tokenResponse .accessToken());
        // then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName())
                .isEqualTo("1");
    }

    @DisplayName("유효한 토큰을 검증한다")
    @Test
    void validateValidToken() {
        // given
        TokenResponse tokenResponse = jwtProvider.createTokens(memberId, role);
        // when
        boolean result = jwtProvider.isNotValidateToken(tokenResponse.accessToken());
        // then
        assertThat(result)
                .isFalse();
    }

    @DisplayName("유효하지 않은 토큰을 검증한다")
    @Test
    void validateTokenInInvalidToken() {
        // given
        String invalidToken = "유효하지 않은 토큰";
        // when
        boolean result = jwtProvider.isNotValidateToken(invalidToken);
        // then
        assertThat(result)
                .isTrue();
    }
}