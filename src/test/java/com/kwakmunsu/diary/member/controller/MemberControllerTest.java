package com.kwakmunsu.diary.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.global.annotation.TestMember;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import com.kwakmunsu.diary.member.service.dto.MemberInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import({TestSecurityConfig.class})
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    private static final String BASE_URL = "/members";

    @MockBean
    private MemberQueryService memberQueryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("회원 정보를 가져온다.")
    @TestMember
    @Test
    void createMember() throws Exception {
        // given
        MemberInfoResponse response = MemberInfoResponse.builder()
                .email("test@gmail.com")
                .nickname("testNickname")
                .createdAt("2025. 4월 26일")
                .build();

        given(memberQueryService.getMemberInfo(any(Long.class))).willReturn(response);

        mockMvc.perform(
                        get(BASE_URL)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("testNickname"));
    }

}