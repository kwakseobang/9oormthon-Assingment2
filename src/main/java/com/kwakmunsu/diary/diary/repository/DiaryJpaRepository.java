package com.kwakmunsu.diary.diary.repository;

import com.kwakmunsu.diary.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {

    boolean existsByTitle(String title);

}