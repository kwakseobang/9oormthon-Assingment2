package com.kwakmunsu.diary.global.jwt.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {

}