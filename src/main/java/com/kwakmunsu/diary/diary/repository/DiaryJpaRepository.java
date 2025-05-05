package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {

    boolean existsByTitle(String title);

    List<Diary> findByMemberId(Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);

}