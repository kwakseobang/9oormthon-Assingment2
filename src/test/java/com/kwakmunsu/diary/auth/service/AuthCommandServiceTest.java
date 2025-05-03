package com.kwakmunsu.diary.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.MemberCommandService;
import com.kwakmunsu.diary.member.service.MemberQueryService;
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

    @Mock
    private MemberQueryService memberQueryService;

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

    @DisplayName("회원 로그아웃을 하고 RefeshToken을 삭제한다")
    @Test
    void logoutAndDeleteRefreshToken() {
        // given
        Long testMemberId = 1L;
        Member testMember = create();
        given(memberQueryService.findMember(any(Long.class))).willReturn(testMember);

        // when
        authCommandService.logout(testMemberId);

        // then
        verify(memberQueryService).findMember(testMemberId);
        verify(testMember).updateRefreshToken(null);
    }

    private Member create() {
        return mock(Member.class);
    }

    @DisplayName("로그아웃 시 회원이 없으면 예외를 던진다.")
    @Test
    void throwsExceptionWhenNotFoundMember() {
        // given
        Long testMemberId = 1L;
        given(memberQueryService.findMember(any(Long.class)))
                .willThrow(new DiaryNotFoundException(ErrorMessage.NOT_FOUND_MEMBER.getMessage()));

        // when & then
        assertThatThrownBy(() ->  authCommandService.logout(testMemberId))
                .isInstanceOf(DiaryNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_MEMBER.getMessage());
    }

}