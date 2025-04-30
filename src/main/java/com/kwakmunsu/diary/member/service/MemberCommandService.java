package com.kwakmunsu.diary.member.service;

import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;

}