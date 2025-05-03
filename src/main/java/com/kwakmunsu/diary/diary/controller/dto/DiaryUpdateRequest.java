package com.kwakmunsu.diary.diary.controller.dto;

import com.kwakmunsu.diary.diary.annotation.EnumValid;
import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.DiaryUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DiaryUpdateRequest(

        @NotBlank(message = "변경하실 일기 제목을 입력해주세요")
        String title,

        @NotBlank(message = "변경하실 일기 내용을 입력해주세요")
        String content,

        @NotNull(message = "변경하실 공개 범위 값을 입력해주세요")
        @EnumValid(enumClass = AccessScope.class, message = "접근 권한은 PUBLIC 또는 PRIVATE 만 가능합니다")
        String accessScope // "PUBLIC" || "PRIVATE"
) {

    public DiaryUpdateServiceRequest toServiceRequest(Long memberId, Long diaryId) {
        return DiaryUpdateServiceRequest.builder()
                .memberId(memberId)
                .diaryId(diaryId)
                .title(title)
                .content(content)
                .accessScope(AccessScope.valueOf(accessScope))
                .build();
    }

}