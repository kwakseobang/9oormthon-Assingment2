package com.kwakmunsu.diary.diary.service;

import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.response.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiaryQueryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public List<MyDiaryPreviewResponse> getDiariesByMemberId(Long memberId) {
        List<Diary> diaries = diaryRepository.findByMemberId(memberId);
        return diaries.stream()
                .map(MyDiaryPreviewResponse::from)
                .toList();
    }

}