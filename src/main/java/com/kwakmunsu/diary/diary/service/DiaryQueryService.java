package com.kwakmunsu.diary.diary.service;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryQueryService {

    private final DiaryRepository diaryRepository;

    public DiaryPaginationResponse<MyDiaryPreviewResponse> getDiariesByMemberId(
            Long diaryId,
            Long memberId
    ) {
        return diaryRepository.findByMemberId(diaryId, memberId);
    }

    public DiaryPaginationResponse<PublicDiaryPreviewResponse> getDiariesByPublic(Long diaryId) {
        return diaryRepository.findByPublic(diaryId);
    }

    public DiaryDetailResponse getDiary(Long diaryId, Long memberId) {
        validateDiaryOwnership(diaryId, memberId);

        return diaryRepository.findDiaryDetailById(diaryId);
    }

    private void validateDiaryOwnership(Long diaryId, Long authorId) {
        if (!diaryRepository.existsByIdAndMemberId(diaryId, authorId)) {
            throw new DiaryUnAuthenticationException("읽기 권한이 없습니다.");
        }
    }

    public DiaryDetailResponse getDiaryByAccessScope(Long diaryId) {
        validateDiaryPublic(diaryId);

        return diaryRepository.findDiaryDetailById(diaryId);
    }

    public DiaryPaginationResponse<PublicDiaryPreviewResponse> search(
            Long diaryId,
            String keyword
    ) {
        return diaryRepository.search(diaryId, keyword);
    }

    private void validateDiaryPublic(Long diaryId) {
        if (!diaryRepository.existsByIdAndAccessScope(diaryId, AccessScope.PUBLIC)) {
            throw new DiaryUnAuthenticationException("공개 되지 않은 일기입니다.");
        }
    }

}