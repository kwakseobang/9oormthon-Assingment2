package com.kwakmunsu.diary.diary.service.dto.request;

import lombok.Builder;

@Builder
public record DiaryCreateServiceRequest(
        Long memberId,

        String title,

        String content,

        String accessScope // "PUBLIC" || "PRIVATE"
) {

}