package com.kwakmunsu.diary.auth.controller;

import static com.kwakmunsu.diary.global.jwt.common.TokenType.REFRESH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.auth.controller.dto.MemberCreateRequest;
import com.kwakmunsu.diary.auth.controller.dto.MemberLoginRequest;
import com.kwakmunsu.diary.auth.service.AuthCommandService;
import com.kwakmunsu.diary.auth.service.AuthQueryService;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.jwt.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String BASE_URL = "/auth";

    @MockBean
    private AuthCommandService authCommandService;

    @MockBean
    private AuthQueryService authQueryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final String testEmail = "test@gmail.com";
    final String testPassword = "12345678";
    final String testNickname = "testNickname";


    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() throws Exception {
        // given
        long testMemberId = 1L;
        MemberCreateRequest request = createRequest(testEmail, testPassword, testNickname);

        given(authCommandService.signUp(request.toServiceRequest())).willReturn(testMemberId);

        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + testMemberId));
    }

    @DisplayName("회원을 생성 실패 시 예외를 반환한다.")
    @Test
    void throwsExceptionWhenFailCreateMember() throws Exception {
        // given
        MemberCreateRequest request = createRequest(testEmail, testPassword, testNickname);

        given(authCommandService.signUp(request.toServiceRequest()))
                .willThrow(new DiaryDuplicationException("x"));

        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("회원 생성 요청 값 유효성 테스트")
    @ParameterizedTest
    @CsvSource({
            ", 12345678, 닉네임, email을 입력해주세요",
            "bademail, 12345678, 닉네임, 이메일 형식이 올바르지 않습니다.",
            "test@gmail.com, 12345678, , 닉네임을 입력해주세요",
            "test@gmail.com, , 닉네임, 비밀번호를 입력해주세요",
            "test@gmail.com, 12345, 닉네임, 비밀번호는 최소 8자리 이상입니다"
    })
    void shouldFailValidationCreateMember(
            String email,
            String password,
            String nickname,
            String expectedMessage
    ) throws Exception {
        // given
        MemberCreateRequest request = createRequest(email, password, nickname);

        // expected
        mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.*").value(expectedMessage));
    }

    private MemberCreateRequest createRequest(String email, String password, String nickname) {
        return MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    @DisplayName("로그인을 한다")
    @Test
    void login() throws Exception {
        // given
        MemberLoginRequest request = loginRequest(testEmail, testPassword);
        TokenResponse testTokenResponse = TokenResponse.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();

        given(authQueryService.login(request.toServiceRequest())).willReturn(testTokenResponse);

        // expected
        mockMvc.perform(
                        post(BASE_URL + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testAccessToken"))
                .andExpect(cookie().value(REFRESH.getValue(), "testRefreshToken"));
    }

    @DisplayName("로그인 실패 시 예외를 던진다")
    @Test
    void throwsExceptionWhenFailLogin() throws Exception {
        // given
        MemberLoginRequest request = loginRequest(testEmail, testPassword);

        given(authQueryService.login(request.toServiceRequest())).willThrow(
                new DiaryUnAuthenticationException("x")
        );

        // expected
        mockMvc.perform(
                        post(BASE_URL + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 요청 값 유효성 테스트")
    @ParameterizedTest
    @CsvSource({
            ", 12345678, email을 입력해주세요",
            "bademail, 12345678, 이메일 형식이 올바르지 않습니다.",
            "test@gmail.com, , 비밀번호를 입력해주세요",
            "test@gmail.com, 12345, 비밀번호는 최소 8자리 이상입니다"
    })
    void shouldFailValidationLogin(
            String email,
            String password,
            String expectedMessage
    ) throws Exception {
        // given
        MemberLoginRequest request = loginRequest(email, password);

        // expected
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.*").value(expectedMessage));
    }

    private MemberLoginRequest loginRequest(String email, String password) {
        return MemberLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    @DisplayName("AT와 RT를 재발급하고 RT는 쿠키에 저장한다")
    @Test
    void reissueToken() throws Exception {
        // given
        String testRefreshToken = "testRefreshToken";
        TokenResponse testTokenResponse = TokenResponse.builder()
                .accessToken("testAccessToken")
                .refreshToken(testRefreshToken)
                .build();

        given(authQueryService.reissue(any())).willReturn(testTokenResponse);

        // expected
        mockMvc.perform(
                        post(BASE_URL + "/reissue")
                                .cookie(new Cookie("refreshToken", testRefreshToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testAccessToken"))
                .andExpect(cookie().value(REFRESH.getValue(), testRefreshToken));
    }

}