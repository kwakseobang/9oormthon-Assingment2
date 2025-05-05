package com.kwakmunsu.diary.diary.service.dto.response;

import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PublicDiaryPreviewResponse(
        Long diaryId,
        String title,
        String nickname,
        String createAt
) {

    public static PublicDiaryPreviewResponse from(
            Long diaryId,
            String title,
            String nickname,
            LocalDateTime createAt
    ) {
        return PublicDiaryPreviewResponse.builder()
                .diaryId(diaryId)
                .title(title)
                .nickname(nickname)
                .createAt(datetimeToString(createAt))
                .build();
    }

}