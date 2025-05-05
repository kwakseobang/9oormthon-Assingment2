package com.kwakmunsu.diary.diary.controller.dto;

import com.kwakmunsu.diary.diary.annotation.EnumValid;
import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.request.DiaryCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DiaryCreateRequest(

        @NotBlank(message = "일기 제목을 입력해주세요")
        String title,

        @NotBlank(message = "일기 내용을 입력해주세요")
        String content,

        @EnumValid(enumClass = AccessScope.class, message = "PUBLIC 또는 PRIVATE 를 정확히 입력해주세요")
        String accessScope // "PUBLIC" || "PRIVATE"
) {

    public DiaryCreateServiceRequest toServiceRequest(Long memberId) {
        return DiaryCreateServiceRequest.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .accessScope(accessScope)
                .build();
    }

}