package com.kwakmunsu.diary.auth.service.dto;

import lombok.Builder;

@Builder
public record MemberCreateServiceRequest(
        String email,
        String password,
        String nickname
) {

}