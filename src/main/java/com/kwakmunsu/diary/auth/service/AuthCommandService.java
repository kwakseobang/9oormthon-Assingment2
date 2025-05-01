package com.kwakmunsu.diary.auth.service;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.MemberCommandService;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthCommandService {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public Long signUp(MemberCreateServiceRequest request) {
        return memberCommandService.create(request);
    }

    public void logout(Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        member.updateRefreshToken(null); // 로그아웃 시 삭제해주기 위해 null 사용
    }

}