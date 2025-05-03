package com.kwakmunsu.diary.member.controller;

import com.kwakmunsu.diary.global.annotation.CurrentLoginMember;
import com.kwakmunsu.diary.member.service.MemberQueryService;
import com.kwakmunsu.diary.member.service.dto.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMember(@CurrentLoginMember Long memberId) {
        log.info(memberId.toString());
        MemberInfoResponse response = memberQueryService.getMemberInfo(memberId);
        return ResponseEntity.ok(response);
    }

}