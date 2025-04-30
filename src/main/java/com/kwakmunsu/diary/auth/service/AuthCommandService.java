package com.kwakmunsu.diary.auth.service;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import com.kwakmunsu.diary.member.service.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthCommandService {

    private final MemberCommandService memberCommandService;

    public Long signUp(MemberCreateServiceRequest request) {
        return memberCommandService.create(request);
    }

}