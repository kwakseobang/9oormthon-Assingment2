package com.kwakmunsu.diary.global.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class DiaryUnAuthenticationException extends DiaryException {

    public DiaryUnAuthenticationException( String message) {
        super(UNAUTHORIZED, message);
    }

}