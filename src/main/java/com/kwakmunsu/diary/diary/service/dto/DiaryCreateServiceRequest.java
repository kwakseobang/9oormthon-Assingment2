package com.kwakmunsu.diary.diary.service.dto;

import lombok.Builder;

@Builder
public record DiaryCreateServiceRequest(
        Long memberId,

        String title,

        String content,

        String accessLevel // "PUBLIC" || "PRIVATE"
) {

}