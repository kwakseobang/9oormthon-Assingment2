package com.kwakmunsu.diary.auth.controller.dto;

import com.kwakmunsu.diary.auth.anotation.ValidEmail;
import com.kwakmunsu.diary.auth.anotation.ValidPassword;
import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberCreateRequest(

        @ValidEmail
        String email,

        @ValidPassword
        String password,

        @NotBlank(message = "닉네임을 입력해주세요")
        String nickname
) {

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

}