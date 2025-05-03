package com.kwakmunsu.diary.diary.service.dto.response;

import static com.kwakmunsu.diary.util.TimeConverter.datetimeToString;

import com.kwakmunsu.diary.diary.entity.Diary;
import lombok.Builder;

@Builder
public record MyDiaryPreviewResponse(
        Long diaryId,
        String title,
        String accessScope,
        String createAt
) {

    public static MyDiaryPreviewResponse from(Diary diary) {
        return MyDiaryPreviewResponse.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .accessScope(diary.getAccessScope().name())
                .createAt(datetimeToString(diary.getCreatedAt()))
                .build();
    }

}