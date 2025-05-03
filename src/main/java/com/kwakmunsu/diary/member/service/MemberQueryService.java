package com.kwakmunsu.diary.member.service;

import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;

import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.dto.MemberInfoResponse;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import com.kwakmunsu.diary.util.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public Member findMember(MemberLoginServiceRequest request) {
        Member member = memberRepository.findByEmail(request.email());
        validatePassword(request.password(), member.getPassword());
        return member;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .createdAt(datetimeToString(member.getCreatedAt()))
                .build();
    }

    private void validatePassword(String rawPassword, String encryptedPassword) {
        // matches(평문 패스워드, 암호화 패스워드) 순서로 해야 됨.
        if (!bCryptPasswordEncoder.matches(rawPassword, encryptedPassword)) {
            throw new DiaryUnAuthenticationException(ErrorMessage.UNAUTHORIZED_ERROR.getMessage());
        }
    }

    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

}