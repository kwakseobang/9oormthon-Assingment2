package com.kwakmunsu.diary.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.kwakmunsu.diary.global.exception.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예외 발생: " + e.getMessage());

        int statusCode = INTERNAL_SERVER_ERROR.value();
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(statusCode)
                .status(INTERNAL_SERVER_ERROR.toString())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        int statusCode = e.getStatusCode().value();
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(statusCode)
                .status(e.getStatusCode().toString())
                .message("잘못된 요청입니다.")
                .build();

        e.getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            log.error("유효성 검사 실패 - 필드: {}, 메시지: {}", field, errorMessage);
            response.addValidation(field, errorMessage);
        });

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        int statusCode = BAD_REQUEST.value();
        String paramName = e.getParameter().getParameterName();
        String paramType = e.getParameter().getParameterType().getSimpleName();
        String detailMessage = e.getMessage();
        String message =
                "[" + paramName + "] 파라미터는 " + paramType + " 타입이어야 합니다. 상세: " + detailMessage;

        ErrorResponse response = ErrorResponse.builder()
                .statusCode(statusCode)
                .status(BAD_REQUEST.toString())
                .message(message)
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        int statusCode = BAD_REQUEST.value();
        String paramName = e.getParameterName();
        String paramType = e.getParameterType();
        String message = paramType + "타입의" + " [" + paramName + " ] " + "파라미터가 누락되었습니다.";

        ErrorResponse response = ErrorResponse.builder()
                .statusCode(statusCode)
                .status(BAD_REQUEST.toString())
                .message(message)
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(DiaryException e) {
        log.error("DiaryException 예외: 상태코드 - {}, 메세지 - {}", e.getHttpStatus(), e.getMessage());

        int statusCode = e.getHttpStatus().value();
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(statusCode)
                .status(e.getHttpStatus().toString())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

}