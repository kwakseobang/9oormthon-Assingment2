package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryJpaRepository diaryJpaRepository;

    @Override
    public Long save(Diary diary) {
        return diaryJpaRepository.save(diary).getId();
    }

    @Override
    public boolean existsByTitle(String title) {
        return false;
    }
}