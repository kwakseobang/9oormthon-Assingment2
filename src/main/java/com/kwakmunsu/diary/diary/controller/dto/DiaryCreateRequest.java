package com.kwakmunsu.diary.diary.controller.dto;

import com.kwakmunsu.diary.diary.annotation.EnumValid;
import com.kwakmunsu.diary.diary.entity.AccessLevel;
import com.kwakmunsu.diary.diary.service.dto.DiaryCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DiaryCreateRequest(

        @NotBlank(message = "일기 제목을 입력해주세요")
        String title,

        @NotBlank(message = "일기 내용을 입력해주세요")
        String content,

        @NotNull(message = "값을 입력해주세요")
        @EnumValid(enumClass = AccessLevel.class, message = "접근 권한은 PUBLIC 또는 PRIVATE 만 가능합니다")
        String accessLevel // "PUBLIC" || "PRIVATE"
) {

    public DiaryCreateServiceRequest toServiceRequest(Long memberId) {
        return DiaryCreateServiceRequest.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .accessLevel(accessLevel)
                .build();
    }

}