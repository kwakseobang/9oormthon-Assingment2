package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import java.util.List;
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
        return diaryJpaRepository.existsByTitle(title);
    }

    @Override
    public Diary findById(Long diaryId) {
        return diaryJpaRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(
                        ErrorMessage.NOT_FOUND_DIARY.getMessage())
                );
    }

    @Override
    public List<Diary> findByMemberId(Long memberId) {
        return diaryJpaRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteById(Long diaryId) {
         diaryJpaRepository.deleteById(diaryId);
    }
}