package com.kwakmunsu.diary.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.auth.controller.dto.MemberCreateRequest;
import com.kwakmunsu.diary.auth.service.AuthCommandService;
import com.kwakmunsu.diary.auth.service.AuthQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() throws Exception {
        // given
        long testMemberId = 1L;
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("testEmail")
                .password("testPassword")
                .nickname("testNickname")
                .build();

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

    @DisplayName("회원을 생성할떄는 이메일은 필수입니다.")
    @Test
    void shouldFailWhenEmailIsMissing() throws Exception {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .password("testPassword")
                .nickname("testNickname")
                .build();
        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.validation.email").value("email을 입력해주세요"));
    }

    @DisplayName("회원을 생성할떄는 닉네임은 필수입니다.")
    @Test
    void shouldFailWhenNicknameIsMissing() throws Exception {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("testEmail")
                .password("testPassword")
                .build();
        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임을 입력해주세요"));
    }

    @DisplayName("회원을 생성할떄는 비밀번호는 필수입니다.")
    @Test
    void shouldFailWhenPasswordIsMissing() throws Exception {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("testEmail")
                .nickname("testNickname")
                .build();
        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.validation.password").value("비밀번호를 입력해주세요"));
    }

    @DisplayName("회원을 생성할떄는 비밀번호 8자리 이상입니다.")
    @Test
    void shouldFailWhenPasswordIsShorterThan8Characters() throws Exception {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("testEmail")
                .password("12345")
                .nickname("testNickname")
                .build();
        // expected
        mockMvc.perform(
                        post(BASE_URL + "/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 최소 8자리 이상입니다"));
    }

}