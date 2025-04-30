package com.kwakmunsu.diary.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DiaryNotFoundException extends DiaryException {

    public DiaryNotFoundException(String message) {
        super(NOT_FOUND, message);
    }

}