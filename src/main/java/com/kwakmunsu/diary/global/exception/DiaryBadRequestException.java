package com.kwakmunsu.diary.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DiaryBadRequestException extends DiaryException {

    public DiaryBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }

}