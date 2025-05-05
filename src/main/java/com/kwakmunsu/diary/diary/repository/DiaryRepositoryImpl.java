package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryJpaRepository diaryJpaRepository;
    private final DiaryQueryDslRepository diaryQueryDslRepository;

    @Override
    public Long save(Diary diary) {
        return diaryJpaRepository.save(diary).getId();
    }

    @Override
    public boolean existsByTitle(String title) {
        return diaryJpaRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByIdAndMemberId(Long diaryId, Long memberId) {
        return diaryJpaRepository.existsByIdAndMemberId(diaryId, memberId);
    }

    @Override
    public boolean existsByIdAndAccessScope(Long diaryId, AccessScope accessScope) {
        return diaryJpaRepository.existsByIdAndAccessScope(diaryId, accessScope);
    }

    @Override
    public Diary findById(Long diaryId) {
        return diaryJpaRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(
                        ErrorMessage.NOT_FOUND_DIARY.getMessage())
                );
    }

    @Override
    public DiaryPaginationResponse<PublicDiaryPreviewResponse> findByPublic(Long diaryId) {
        return diaryQueryDslRepository.findByPublic(diaryId);
    }

    @Override
    public DiaryDetailResponse findDiaryDetailById(Long diaryId) {
        return diaryQueryDslRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(
                        ErrorMessage.NOT_FOUND_DIARY.getMessage())
                );
    }

    @Override
    public DiaryPaginationResponse<MyDiaryPreviewResponse> findByMemberId(
            Long diaryId,
            Long memberId
    ) {
        return diaryQueryDslRepository.findDiariesByMemberId(diaryId, memberId);
    }

    @Override
    public void deleteById(Long diaryId) {
        diaryJpaRepository.deleteById(diaryId);
    }

}