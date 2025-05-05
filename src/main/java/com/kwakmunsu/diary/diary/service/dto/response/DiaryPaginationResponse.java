package com.kwakmunsu.diary.diary.service.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record DiaryPaginationResponse<T extends DiaryPreviewResponse>(
        List<T> diaries,
        boolean hasNext,
        Long nextCursorId
) {

}