package com.kwakmunsu.diary.diary.service;

import com.kwakmunsu.diary.diary.entity.AccessLevel;
import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.DiaryCreateServiceRequest;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiaryCommandService {

    private final DiaryRepository diaryRepository;

    public Long create(DiaryCreateServiceRequest request) {
        validateDiary(request);
        Diary diary = Diary.builder()
                .memberId(request.memberId())
                .title(request.title())
                .content(request.content())
                .accessLevel(AccessLevel.valueOf(request.accessLevel()))
                .build();

        return diaryRepository.save(diary);
    }

    private void validateDiary(DiaryCreateServiceRequest request) {
        if (diaryRepository.existsByTitle(request.title())) {
            throw new DiaryDuplicationException(
                    ErrorMessage.DUPLICATE_TITLE.getMessage() + request.title()
            );
        }
    }

}