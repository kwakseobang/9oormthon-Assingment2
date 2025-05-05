package com.kwakmunsu.diary.diary.service.dto.response.my;

import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;

import com.kwakmunsu.diary.diary.service.dto.response.DiaryPreviewResponse;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyDiaryPreviewResponse(
        Long id,
        String title,
        String accessScope,
        String createAt
) implements DiaryPreviewResponse {

    public static MyDiaryPreviewResponse from(
            Long id,
            String title,
            String accessScope,
            LocalDateTime createAt
    ) {
        return MyDiaryPreviewResponse.builder()
                .id(id)
                .title(title)
                .accessScope(accessScope)
                .createAt(datetimeToString(createAt))
                .build();
    }

}