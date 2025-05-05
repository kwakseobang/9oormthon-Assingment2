package com.kwakmunsu.diary.diary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.entity.Diary;
import com.kwakmunsu.diary.diary.service.dto.request.DiaryCreateServiceRequest;
import com.kwakmunsu.diary.diary.service.dto.request.DiaryUpdateServiceRequest;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryDuplicationException;
import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DiaryCommandServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private DiaryCommandService diaryCommandService;

    @DisplayName("일기를 생성 후 일기 id를 반환한다")
    @Test
    void returnDiaryIdWhenCreateDiary() {
        // given
        DiaryCreateServiceRequest request = DiaryCreateServiceRequest.builder()
                .memberId(1L)
                .title("testTitle")
                .content("testContent")
                .accessScope("PUBLIC")
                .build();
        given(diaryRepository.save(any(Diary.class))).willReturn(1L);

        // when
        Long diaryId = diaryCommandService.create(request);

        // then
        assertThat(diaryId).isEqualTo(1L);
    }

    @DisplayName("일기 생성 시 제목이 겹치면 예외를 반환한다.")
    @Test
    void throwsExceptionWhenAlreadyDiaryTitle() {
        // given
        DiaryCreateServiceRequest request = DiaryCreateServiceRequest.builder()
                .memberId(1L)
                .title("testTitle")
                .content("testContent")
                .accessScope("PUBLIC")
                .build();
        given(diaryRepository.existsByTitle(any(String.class)))
                .willThrow(new DiaryDuplicationException(
                        ErrorMessage.DUPLICATE_TITLE.getMessage() + "testTitle"));

        // when & then
        assertThatThrownBy(() -> diaryCommandService.create(request))
                .isInstanceOf(DiaryDuplicationException.class)
                .hasMessage(ErrorMessage.DUPLICATE_TITLE.getMessage() + "testTitle");
    }

    @DisplayName("일기 정보를 업데이트 한다")
    @Test
    void updateDiaryStatus() {
        // given
        String updateTestTitle = "updateTestTitle";
        String updateTestContent = "updateTestContent";
        DiaryUpdateServiceRequest request = DiaryUpdateServiceRequest.builder()
                .memberId(1L)
                .diaryId(1L)
                .title(updateTestTitle)
                .content(updateTestContent)
                .accessScope(AccessScope.PRIVATE)
                .build();
        Member member = getMember();
        Diary diary = getDiary("testTitle", "testContent", member);
        given(diaryRepository.findById(any(Long.class))).willReturn(diary);

        // when
        diaryCommandService.update(request);

        // then
        verify(diaryRepository).findById(request.diaryId());
        assertThat(diary.getTitle()).isEqualTo(updateTestTitle);
        assertThat(diary.getContent()).isEqualTo(updateTestContent);
        assertThat(diary.getAccessScope()).isEqualTo(AccessScope.PRIVATE);
    }

    @DisplayName("일기 작성자가 아닌데 수정 시 예외를 반환한다.")
    @Test
    void throwsExceptionWhenUpdateDiaryByNotOwner() {
        // given
        DiaryUpdateServiceRequest request = DiaryUpdateServiceRequest.builder()
                .memberId(2L)
                .diaryId(1L)
                .build();
        Member member = getMember();
        Diary diary = getDiary("testTitle", "testContent", member);
        given(diaryRepository.findById(any(Long.class))).willReturn(diary);

        // when & then
        assertThatThrownBy(() -> diaryCommandService.update(request))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage("수정/삭제 권한이 없습니다.");
    }

    @DisplayName("일기를 삭제한다")
    @Test
    void deleteDiary() {
        // given
        Long testDiaryId = 1L;
        Long testMemberId = 1L;
        Member member = getMember();
        Diary diary = getDiary("testTitle", "testContent", member);
        given(diaryRepository.findById(any(Long.class))).willReturn(diary);

        // when
        diaryCommandService.delete(testDiaryId, testMemberId);

        // then
        verify(diaryRepository).deleteById(1L);
    }

    @DisplayName("일기 작성자가 아닌데 삭제 시 예외를 반환한다.")
    @Test
    void throwsExceptionWhenDeleteDiaryByNotOwner() {
        // given
        Long testDiaryId = 1L;
        Long testMemberId = 2L;
        Member member = getMember();
        Diary diary = getDiary("testTitle", "testContent", member);
        given(diaryRepository.findById(any(Long.class))).willReturn(diary);

        // when & then
        assertThatThrownBy(() -> diaryCommandService.delete(testDiaryId, testMemberId))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage("수정/삭제 권한이 없습니다.");
    }

    @DisplayName("일기가 존재하지 않는데 수정/삭제할려고 하면 예외를 던진다 ")
    @Test
    void throwsExceptionWhenNotExistsDiary() {
        // given
        given(diaryRepository.findById(any(Long.class))).willThrow(
                new DiaryNotFoundException(ErrorMessage.NOT_FOUND_DIARY.getMessage()));
        // when & then
        assertThatThrownBy(() -> diaryCommandService.delete(1L, 1L))
                .isInstanceOf(DiaryNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_DIARY.getMessage());
    }

    private Diary getDiary(String title, String content, Member member) {
        return Diary.builder()
                .member(member)
                .title(title)
                .content(content)
                .accessScope(AccessScope.PRIVATE)
                .build();
    }

    private Member getMember() {
        Member member = Member.createMember("test@example.com", "password123", "testUser");
        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }

}