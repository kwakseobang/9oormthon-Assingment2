package com.kwakmunsu.diary.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.global.annotation.TestMember;
import com.kwakmunsu.diary.global.config.WebMvcConfigTest;
import com.kwakmunsu.diary.global.jwt.token.JwtProvider;
import com.kwakmunsu.diary.member.entity.Role;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import com.kwakmunsu.diary.member.service.dto.MemberInfoResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    private static final String BASE_URL = "/members";

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    private JwtProvider jwtProvider; // jwtfilter 떄문에 등록

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // FIXME : 해결 못함 일단 다음 단계..
    @DisplayName("회원 정보를 가져온다.")
//    @TestMember
    @Test
    void createMember() throws Exception {
        // given
        String testToken = "asdiaiuosydiauyiu21i3ui1iuasidhakjbnrfqnoirh0qii";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); //
        MemberInfoResponse response = MemberInfoResponse.builder()
                .email("test@gmail.com")
                .nickname("testNickname")
                .createdAt("2025. 4월 26일")
                .build();

        given(memberQueryService.getMemberInfo(any(Long.class))).willReturn(response);
        mockMvc.perform(
                        get(BASE_URL)
                                .header("Authorization", "Bearer " + testToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("testNickname"));
    }

}