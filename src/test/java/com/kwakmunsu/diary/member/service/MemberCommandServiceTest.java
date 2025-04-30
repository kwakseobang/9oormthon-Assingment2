package com.kwakmunsu.diary.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberCommandService memberCommandService;

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

        given(memberRepository.create(any(Member.class))).willReturn(testMemberId);

        // when
        Long memberId = memberCommandService.create(request);

        // then
        assertThat(memberId).isEqualTo(testMemberId);
    }

    @DisplayName("회원 생성 시 email이 중복이면 예외를 반환한다.")
    @Test
    void throwsExceptionWhenAlreadyExistsEmail() {
        // given
        String testEmail = "test@gmail.com";
        String testPassword = "1234";
        String testNickname = "testNickname";
        MemberCreateServiceRequest request = new MemberCreateServiceRequest(
                testEmail, testPassword, testNickname);

        given(memberRepository.existsByEmail(any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberCommandService.create(request))
                .isInstanceOf(DiaryDuplicationException.class)
                .hasMessage(ErrorMessage.DUPLICATE_EMAIL.getMessage() + testEmail);
    }

    @DisplayName("회원 생성 시 nickname이 중복이면 예외를 반환한다.")
    @Test
    void throwsExceptionWhenAlreadyExistsNickname() {
        // given
        String testEmail = "test@gmail.com";
        String testPassword = "1234";
        String testNickname = "testNickname";
        MemberCreateServiceRequest request = new MemberCreateServiceRequest(
                testEmail, testPassword, testNickname);

        given(memberRepository.existsByNickname(any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberCommandService.create(request))
                .isInstanceOf(DiaryDuplicationException.class)
                .hasMessage(ErrorMessage.DUPLICATE_NICKNAME.getMessage() + testNickname);
    }

}