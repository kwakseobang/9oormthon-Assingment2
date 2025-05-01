package com.kwakmunsu.diary.global.jwt.common;

import lombok.Getter;

@Getter
public enum TokenType {

    AUTHORIZATION_HEADER("Authorization"),
    BEARER_PREFIX("Bearer "),
    ACCESS("accessToken"),
    REFRESH("refreshToken"),
    ;

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

}