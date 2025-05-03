package com.kwakmunsu.diary.diary.entity.dto;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import lombok.Builder;

@Builder
public record DiaryUpdateDomainRequest(

        String title,

        String content,

        AccessScope accessScope // "PUBLIC" || "PRIVATE"

) {

}