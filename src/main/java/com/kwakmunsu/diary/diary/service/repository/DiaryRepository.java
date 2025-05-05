package com.kwakmunsu.diary.diary.service.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.PublicDiaryPreviewResponse;
import java.util.List;

public interface DiaryRepository {

    Long save(Diary diary);

    boolean existsByTitle(String title);

    boolean existsByIdAndMemberId(Long diaryId, Long memberId);

    Diary findById(Long diaryId);

    List<PublicDiaryPreviewResponse> findByPublic();

    DiaryDetailResponse findDiaryDetailById(Long diaryId);

    void deleteById(Long diaryId);

    List<Diary> findByMemberId(Long memberId);

}