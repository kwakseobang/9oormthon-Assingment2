package com.kwakmunsu.diary.auth.service.dto;

import lombok.Builder;

@Builder
public record MemberLoginServiceRequest(
        String email,
        String password
) {

}