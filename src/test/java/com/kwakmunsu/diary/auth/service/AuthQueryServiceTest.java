package com.kwakmunsu.diary.auth.service;

import static com.kwakmunsu.diary.member.entity.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.global.jwt.dto.TokenResponse;
import com.kwakmunsu.diary.global.jwt.token.JwtProvider;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthQueryServiceTest {

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthQueryService authQueryService;

    @DisplayName("로그인을 성공하면 AT와 RT를 발급한다.")
    @Test
    void returnTokenResponseWhenLoginSuccess() {
        // given
        MemberLoginServiceRequest request = MemberLoginServiceRequest.builder()
                .email("test@gmail.com")
                .password("12345678")
                .build();
        TokenResponse testTokenResponse = new TokenResponse("testAccessToken", "testRefreshToken");
        Member testMember = create();
        given(jwtProvider.isNotValidateToken(any())).willReturn(false);
        given(memberQueryService.findMember(any())).willReturn(testMember);
        given(jwtProvider.createTokens(any(), any())).willReturn(testTokenResponse);

        // when
        TokenResponse tokenResponse = authQueryService.login(request);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isEqualTo(testTokenResponse.accessToken());
        assertThat(tokenResponse.refreshToken()).isEqualTo(testTokenResponse.refreshToken());
    }

    private Member create() {
        return createMember("test@gmail.com", "12345678", "testNickname");
    }

    @DisplayName("토큰을 재발급한다")
    @Test
    void reissueToken() {
        // given
        String testRefreshToken = "testRefreshToken";
        Member testMember = create();
        TokenResponse testTokenResponse = new TokenResponse("testAccessToken", "testRefreshToken");
        given(jwtProvider.isNotValidateToken(any())).willReturn(false);
        given(memberQueryService.findByRefreshToken(any())).willReturn(testMember);
        given(jwtProvider.createTokens(any(), any())).willReturn(testTokenResponse);

        // when
        TokenResponse tokenResponse = authQueryService.reissue(testRefreshToken);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isEqualTo(testTokenResponse.accessToken());
        assertThat(tokenResponse.refreshToken()).isEqualTo(testTokenResponse.refreshToken());
    }

    @DisplayName("유효한 토큰이 아닐 경우 예외를 던진다.")
    @Test
    void throwsExceptionWhenInvalidToken() {
        // given
        String testRefreshToken = "testRefreshToken";
        given(jwtProvider.isNotValidateToken(any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authQueryService.reissue(testRefreshToken))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage(ErrorMessage.INVALID_TOKEN.getMessage());
    }
}