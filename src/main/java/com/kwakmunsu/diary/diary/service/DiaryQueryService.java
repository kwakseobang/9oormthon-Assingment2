package com.kwakmunsu.diary.diary.service;

import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiaryQueryService {

    private final DiaryRepository diaryRepository;

}