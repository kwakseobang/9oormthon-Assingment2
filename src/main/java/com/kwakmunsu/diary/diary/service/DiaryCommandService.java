package com.kwakmunsu.diary.diary.service;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.request.DiaryCreateServiceRequest;
import com.kwakmunsu.diary.diary.service.dto.request.DiaryUpdateServiceRequest;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryCommandService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public Long create(DiaryCreateServiceRequest request) {
        validateTitleUniqueness(request.title());

        Member member = memberRepository.findById(request.memberId());
        Diary diary = Diary.builder()
                .member(member)
                .title(request.title())
                .content(request.content())
                .accessScope(AccessScope.valueOf(request.accessScope()))
                .build();

        return diaryRepository.save(diary);
    }

    private void validateTitleUniqueness(String title) {
        if (diaryRepository.existsByTitle(title)) {
            throw new DiaryDuplicationException(ErrorMessage.DUPLICATE_TITLE.getMessage() + title);
        }
    }

    @Transactional
    public void update(DiaryUpdateServiceRequest request) {
        Long authorId = request.memberId();
        Diary diary = diaryRepository.findById(request.diaryId());

        validateDiaryOwnership(diary, authorId);

        diary.updateDiary(request.toDomainRequest());
    }

    @Transactional
    public void delete(Long diaryId, Long authorId) {
        Diary diary = diaryRepository.findById(diaryId);

        validateDiaryOwnership(diary, authorId);

        diaryRepository.deleteById(diaryId);
    }

    private void validateDiaryOwnership(Diary diary, Long authorId) {
        if (diary.isNotAuthor(authorId)) {
            throw new DiaryUnAuthenticationException("수정/삭제 권한이 없습니다.");
        }
    }


}