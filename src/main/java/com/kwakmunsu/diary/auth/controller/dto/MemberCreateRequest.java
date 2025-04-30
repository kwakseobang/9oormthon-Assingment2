package com.kwakmunsu.diary.auth.controller.dto;

import com.kwakmunsu.diary.auth.service.dto.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MemberCreateRequest(

        @NotBlank(message = "email을 입력해주세요")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, message = "비밀번호는 최소 8자리 이상입니다")
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