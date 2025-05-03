package com.kwakmunsu.diary.diary.service.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import java.util.List;

public interface DiaryRepository {

    Long save(Diary diary);

    boolean existsByTitle(String title);

    Diary findById(Long diaryId);

    void deleteById(Long diaryId);

    List<Diary> findByMemberId(Long memberId);

}