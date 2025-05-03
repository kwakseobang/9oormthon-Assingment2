package com.kwakmunsu.diary.member.service;

import static com.kwakmunsu.diary.member.entity.Member.createMember;
import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.dto.MemberInfoResponse;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberQueryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private MemberQueryService memberQueryService;

    final String testEmail = "test@gmail.com";

    final String testPassword = "12345678";

    @DisplayName("회원을 반환한다")
    @Test
    void returnMember() {
        // given
        MemberLoginServiceRequest request = MemberLoginServiceRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();
        Member testMember = create();
        given(memberRepository.findByEmail(request.email())).willReturn(testMember);
        given(bCryptPasswordEncoder.matches(any(), any())).willReturn(true);

        // when
        Member member = memberQueryService.findMember(request);
        // then
        assertThat(member.getEmail()).isEqualTo(testEmail);
        assertThat(member.getPassword()).isEqualTo(testPassword);
    }

    @DisplayName("로그인 시 이메일이 존재하지 않으면 예외를 반환한다.")
    @Test
    void throwsExceptionWhenNotExistsEmail() {
        // given
        MemberLoginServiceRequest request = MemberLoginServiceRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();
        given(memberRepository.findByEmail(request.email()))
                .willThrow(new DiaryNotFoundException(ErrorMessage.NOT_FOUND_MEMBER.getMessage()));

        // when & then
        assertThatThrownBy(() -> memberQueryService.findMember(request))
                .isInstanceOf(DiaryNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_MEMBER.getMessage());
    }

    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 예외를 반환한다.")
    @Test
    void throwsExceptionWhenMissMatchesPassword() {
        // given
        MemberLoginServiceRequest request = MemberLoginServiceRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();
        Member testMember = create();
        given(memberRepository.findByEmail(request.email())).willReturn(testMember);
        given(bCryptPasswordEncoder.matches(any(), any()))
                .willThrow(new DiaryUnAuthenticationException(
                        ErrorMessage.UNAUTHORIZED_ERROR.getMessage())
                );

        // when & then
        assertThatThrownBy(() -> memberQueryService.findMember(request))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage(ErrorMessage.UNAUTHORIZED_ERROR.getMessage());
    }

    private Member create() {
        return createMember(testEmail, testPassword, "testNickname");
    }

    @DisplayName("회원 정보를 가져온다")
    @Test
    void getMemberInfo() {
        // given
        Long testMemberId = 1L;
        Member mockMember = createMockMember();
        given(memberRepository.findById(any(Long.class))).willReturn(mockMember);

        // when
        MemberInfoResponse response = memberQueryService.getMemberInfo(testMemberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(mockMember.getEmail());
        assertThat(response.createdAt()).isEqualTo(datetimeToString(mockMember.getCreatedAt()));
    }

    private Member createMockMember() {
        Member mockMember = mock(Member.class);
        when(mockMember.getEmail()).thenReturn("test@gmail.com");
        when(mockMember.getNickname()).thenReturn("testNickname");
        when(mockMember.getCreatedAt()).thenReturn(LocalDateTime.now());
        return mockMember;
    }

}