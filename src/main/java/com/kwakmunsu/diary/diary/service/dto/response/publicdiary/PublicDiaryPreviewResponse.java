package com.kwakmunsu.diary.diary.service.dto.response.publicdiary;

import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;

import com.kwakmunsu.diary.diary.service.dto.response.DiaryPreviewResponse;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PublicDiaryPreviewResponse(
        Long id,
        String title,
        String nickname,
        String createAt
) implements DiaryPreviewResponse {

    public static PublicDiaryPreviewResponse from(
            Long id,
            String title,
            String nickname,
            LocalDateTime createAt
    ) {
        return PublicDiaryPreviewResponse.builder()
                .id(id)
                .title(title)
                .nickname(nickname)
                .createAt(datetimeToString(createAt))
                .build();
    }

}