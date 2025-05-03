package com.kwakmunsu.diary.global.exception.dto;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    // <=============== MEMBER ===============>
    NOT_FOUND_MEMBER("ERROR - 회원을 찾을 수 없습니다."),
    BAD_REQUEST_MEMBER("ERROR - 잘못된 회원 요청"),
    BAD_REQUEST_PASSWORD("ERROR - 잘못된 비밀번호 요청"),
    DUPLICATE_EMAIL("ERROR - 회원가입 ID 중복: "),
    DUPLICATE_NICKNAME("ERROR - 회원가입 닉네임 중복: "),

    // <=============== DIARY ===============>
    DUPLICATE_TITLE("ERROR - 일기 제목 중복: "),
    NOT_FOUND_DIARY("ERROR - 해당 일기를 찾을 수 없습니다."),
    BAD_REQUEST_DIARY("ERROR - 잘못된 일기 요청"),

    // <=============== JWT ===============>
    TOKEN_EXPIRED("ERROR - JWT 토큰 만료"),
    INVALID_TOKEN("ERROR - 유효하지 않은 토큰입니다."),
    NOT_FOUND_TOKEN("ERROR - 토큰을 찾을 수 없습니다."),
    BAD_REQUEST_TOKEN("ERROR - 잘못된 토큰 요청"),
    WRONG_AUTH_HEADER("ERROR - [Bearer ]로 시작하는 토큰이 없습니다."),
    TOKEN_VALIDATION_TRY_FAILED("ERROR - 토큰 인증 실패"),

    // <=============== ETC ===============>
    INTERNAL_SERVER_ERROR("ERROR - 서버 내부 에러"),
    UNAUTHORIZED_ERROR("ERROR - 인증되지 않은 사용자입니다."),
    TOKEN_HASH_NOT_SUPPORTED("ERROR - 지원하지 않는 형식의 토큰"),
    FORBIDDEN_ERROR("ERROR - 접근 권한이 없습니다."),
    BAD_REQUEST_ARGUMENT("ERROR - 유효하지 않은 인자입니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

}