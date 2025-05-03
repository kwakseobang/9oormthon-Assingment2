package com.kwakmunsu.diary.diary.service.repository;

import com.kwakmunsu.diary.diary.entity.Diary;

public interface DiaryRepository {

    Long save(Diary diary);

    boolean existsByTitle(String title);

    Diary findById(Long diaryId);

}