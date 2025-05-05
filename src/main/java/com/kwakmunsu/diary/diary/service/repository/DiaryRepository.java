package com.kwakmunsu.diary.diary.service.repository;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;

public interface DiaryRepository {

    Long save(Diary diary);

    boolean existsByTitle(String title);

    boolean existsByIdAndMemberId(Long diaryId, Long memberId);

    boolean existsByIdAndAccessScope(Long diaryId, AccessScope accessScope);

    Diary findById(Long diaryId);

    DiaryPaginationResponse<PublicDiaryPreviewResponse> findByPublic(Long diaryId);

    DiaryDetailResponse findDiaryDetailById(Long diaryId);

    void deleteById(Long diaryId);

    DiaryPaginationResponse<MyDiaryPreviewResponse> findByMemberId(Long diaryId, Long memberId);

}