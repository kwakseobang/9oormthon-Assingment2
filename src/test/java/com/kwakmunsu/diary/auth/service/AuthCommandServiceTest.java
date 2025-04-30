package com.kwakmunsu.diary.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.member.service.MemberCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceTest {

    @Mock
    private MemberCommandService memberCommandService;

    @InjectMocks
    private AuthCommandService authCommandService;

    @DisplayName("회원 생성 성공 시 회원ID를 반환한다.")
    @Test
    void returnMemberIdWhenCreateMember() {
        // given
        Long testMemberId = 1L;
        String testEmail = "test@gmail.com";
        String testPassword = "1234";
        String testNickname = "testNickname";
        MemberCreateServiceRequest request = new MemberCreateServiceRequest(
                testEmail, testPassword, testNickname);
        given(memberCommandService.create(request)).willReturn(testMemberId);

        // when
        Long memberId = authCommandService.signUp(request);

        // then
        assertThat(memberId).isEqualTo(testMemberId);
    }

}