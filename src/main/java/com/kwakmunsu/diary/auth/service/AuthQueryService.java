package com.kwakmunsu.diary.auth.service;

import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import com.kwakmunsu.diary.global.jwt.dto.TokenResponse;
import com.kwakmunsu.diary.global.jwt.token.JwtProvider;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthQueryService {

    private final MemberQueryService memberQueryService;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse login(MemberLoginServiceRequest request) {
        Member member = memberQueryService.findMember(request);
        TokenResponse tokenResponse = jwtProvider.createTokens(member.getId(), member.getRole());

        member.updateRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

}