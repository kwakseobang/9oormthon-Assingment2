package com.kwakmunsu.diary.member.controller;

import com.kwakmunsu.diary.member.service.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberCommandService memberCommandService;

}