package com.kwakmunsu.diary.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DiaryException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected DiaryException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}