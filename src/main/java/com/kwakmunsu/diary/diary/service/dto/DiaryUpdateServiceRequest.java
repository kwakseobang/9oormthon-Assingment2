package com.kwakmunsu.diary.diary.service.dto;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.entity.dto.DiaryUpdateDomainRequest;
import lombok.Builder;

@Builder
public record DiaryUpdateServiceRequest(
        Long memberId,

        Long diaryId,

        String title,

        String content,

        AccessScope accessScope // "PUBLIC" || "PRIVATE"
) {

    public DiaryUpdateDomainRequest toDomainRequest() {
        return DiaryUpdateDomainRequest.builder()
                .title(title)
                .content(content)
                .accessScope(accessScope)
                .build();
    }

}