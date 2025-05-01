package com.kwakmunsu.diary.member.service;

import static com.kwakmunsu.diary.member.entity.Member.createMember;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public Long create(MemberCreateServiceRequest request) {
        String email = request.email();
        String nickname = request.nickname();

        validateEmail(email);
        validateNickname(nickname);

        String encodedPassword = bCryptPasswordEncoder.encode(request.password());

        Member member = createMember(email, encodedPassword, nickname);
        return memberRepository.create(member);
    }


    public void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DiaryDuplicationException(ErrorMessage.DUPLICATE_EMAIL.getMessage() + email);
        }
    }

    public void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DiaryDuplicationException(
                    ErrorMessage.DUPLICATE_NICKNAME.getMessage() + nickname);
        }
    }

}