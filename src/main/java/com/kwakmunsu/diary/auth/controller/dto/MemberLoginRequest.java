package com.kwakmunsu.diary.auth.controller.dto;

import com.kwakmunsu.diary.auth.anotation.ValidEmail;
import com.kwakmunsu.diary.auth.anotation.ValidPassword;
import com.kwakmunsu.diary.auth.service.dto.MemberLoginServiceRequest;
import lombok.Builder;

@Builder
public record MemberLoginRequest(

        @ValidEmail
        String email,

        @ValidPassword
        String password
) {
        public MemberLoginServiceRequest toServiceRequest() {
                return MemberLoginServiceRequest.builder()
                        .email(email)
                        .password(password)
                        .build();
        }

}