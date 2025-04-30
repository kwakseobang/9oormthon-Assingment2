package com.kwakmunsu.diary.global.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class DiaryDuplicationException extends DiaryException {
    
    public DiaryDuplicationException(String message) {
        super(CONFLICT, message);
    }

}