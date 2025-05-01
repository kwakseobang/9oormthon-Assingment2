package com.kwakmunsu.diary.auth.service;

import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
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
        return getTokenResponse(member);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        if (jwtProvider.isNotValidateToken(refreshToken)) {
            throw new DiaryUnAuthenticationException(ErrorMessage.INVALID_TOKEN.getMessage());
        }
        Member member = memberQueryService.findByRefreshToken(refreshToken);
        return getTokenResponse(member);
    }

    private TokenResponse getTokenResponse(Member member) {
        TokenResponse tokenResponse = jwtProvider.createTokens(member.getId(), member.getRole());
        member.updateRefreshToken(tokenResponse.refreshToken());
        return tokenResponse;
    }

}