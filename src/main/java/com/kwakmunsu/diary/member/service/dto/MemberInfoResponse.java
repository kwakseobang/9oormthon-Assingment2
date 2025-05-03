package com.kwakmunsu.diary.member.service.dto;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
        String email,
        String nickname,
        String createdAt
) {

}