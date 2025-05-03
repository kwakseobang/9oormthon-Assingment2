package com.kwakmunsu.diary.diary.service.dto.request;

import lombok.Builder;

@Builder
public record DiaryCreateServiceRequest(
        Long memberId,

        Long diaryId,

        String title,

        String content,

        String accessLevel // "PUBLIC" || "PRIVATE"
) {

}