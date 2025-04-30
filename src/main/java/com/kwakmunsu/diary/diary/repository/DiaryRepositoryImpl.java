package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryJpaRepository diaryJpaRepository;

}