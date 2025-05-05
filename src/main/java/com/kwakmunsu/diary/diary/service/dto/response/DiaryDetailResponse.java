package com.kwakmunsu.diary.diary.service.dto.response;

import com.kwakmunsu.diary.util.TimeConverter;
import com.querydsl.core.Tuple;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record DiaryDetailResponse(
        Long id,
        String title,
        String content,
        String nickname,
        String accessScope,
        String createAt
) {

    public static DiaryDetailResponse from(
            Long id,
            String title,
            String content,
            String nickname,
            String accessScope,
            LocalDateTime createAt
    ) {
        return DiaryDetailResponse.builder()
                .id(id)
                .title(title)
                .content(content)
                .nickname(nickname)
                .accessScope(accessScope)
                .createAt(TimeConverter.datetimeToString(createAt))
                .build();
    }
}