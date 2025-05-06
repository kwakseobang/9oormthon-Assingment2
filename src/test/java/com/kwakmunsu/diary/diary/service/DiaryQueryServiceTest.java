package com.kwakmunsu.diary.diary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.repository.DiaryRepository;
import com.kwakmunsu.diary.global.exception.DiaryUnAuthenticationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryQueryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private DiaryQueryService diaryQueryService;

    @DisplayName("회원 ID로 일기 리스트를 조회한다")
    @Test
    void getDiariesByMemberId() {
        // given
        Long lastDiaryId = 1L;
        Long memberId = 1L;
        List<MyDiaryPreviewResponse> diaries = createMyDiaryPreviewResponse();
        DiaryPaginationResponse<MyDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<MyDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(false)
                .nextCursorId(3L)
                .build();
        given(diaryRepository.findByMemberId(any(Long.class), any(Long.class)))
                .willReturn(expectedResponse);

        // when
        DiaryPaginationResponse<MyDiaryPreviewResponse> actualResponse = diaryQueryService.getDiariesByMemberId(
                lastDiaryId, memberId);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.diaries()).hasSize(expectedResponse.diaries().size());
        assertThat(actualResponse.hasNext()).isFalse();
    }

    @DisplayName("전채 공개된 일기 리스트를 조회한다")
    @Test
    void getDiariesByPublic() {
        // given
        Long lastDiaryId = 1L;
        List<PublicDiaryPreviewResponse> diaries = createPublicDiaryPreviewResponse();
        DiaryPaginationResponse<PublicDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<PublicDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(true)
                .nextCursorId(3L)
                .build();
        given(diaryRepository.findByPublic(any(Long.class)))
                .willReturn(expectedResponse);

        // when
        DiaryPaginationResponse<PublicDiaryPreviewResponse> actualResponse = diaryQueryService.getDiariesByPublic(
                lastDiaryId);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.diaries()).hasSize(expectedResponse.diaries().size());
        assertThat(actualResponse.hasNext()).isTrue();
    }

    @DisplayName("일기 작성자가 자신의 일기를 조회한다")
    @Test
    void getMyDiary() {
        // given
        Long lastDiaryId = 1L;
        Long memberId = 1L;
        DiaryDetailResponse expectedResponse = DiaryDetailResponse.from(
                1L,
                "testTitle",
                "testContent",
                "testNickname",
                "PRIVATE",
                LocalDateTime.now()
        );
        given(diaryRepository.existsByIdAndMemberId(any(Long.class), any(Long.class)))
                .willReturn(true);
        given(diaryRepository.findDiaryDetailById(any(Long.class)))
                .willReturn(expectedResponse);

        // when
        DiaryDetailResponse actualResponse = diaryQueryService.getDiary(lastDiaryId, memberId);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(1L);
        assertThat(actualResponse.title()).isEqualTo(expectedResponse.title());
        assertThat(actualResponse.content()).isEqualTo(expectedResponse.content());
    }

    @DisplayName("자신의 일기를 조회하는데 자신이 작성한 일기가 아닐 경우 예외를 반환한다")
    @Test
    void throwsExceptionWhenNotOwnerDiary() {
        // given
        Long lastDiaryId = 1L;
        Long memberId = 1L;
        given(diaryRepository.existsByIdAndMemberId(any(Long.class), any(Long.class)))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> diaryQueryService.getDiary(lastDiaryId, memberId))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage("읽기 권한이 없습니다.");
    }

    @DisplayName("전채 공개된 일기를 조회한다")
    @Test
    void getDiaryByPublic() {
        // given
        Long diaryId = 1L;
        DiaryDetailResponse expectedResponse = DiaryDetailResponse.from(
                1L,
                "testTitle",
                "testContent",
                "testNickname",
                "PRIVATE",
                LocalDateTime.now()
        );
        given(diaryRepository.existsByIdAndAccessScope(any(Long.class), any(AccessScope.class)))
                .willReturn(true);
        given(diaryRepository.findDiaryDetailById(any(Long.class))).willReturn(expectedResponse);

        // when
        DiaryDetailResponse actualResponse = diaryQueryService.getDiaryByAccessScope(diaryId);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(expectedResponse.id());
        assertThat(actualResponse.title()).isEqualTo(expectedResponse.title());
        assertThat(actualResponse.content()).isEqualTo(expectedResponse.content());
    }

    @DisplayName("전채 공개된 일기를 상세 조회할 때 해당 일기가 PRIVATE 경우 예외를 반환한다")
    @Test
    void throwsExceptionWhenNotPublicDiary() {
        // given
        Long diaryId = 1L;
        given(diaryRepository.existsByIdAndAccessScope(any(Long.class), any(AccessScope.class)))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> diaryQueryService.getDiaryByAccessScope(diaryId))
                .isInstanceOf(DiaryUnAuthenticationException.class)
                .hasMessage("공개 되지 않은 일기입니다.");
    }

    @DisplayName("제목 또는 내용에 키워드가 포함되어 있는 일기를 반환한다")
    @Test
    void searchDiaries() {
        // given
        Long diaryId = 1L;
        String keyword = "test";
        List<PublicDiaryPreviewResponse> diaries = createPublicDiaryPreviewResponse();
        DiaryPaginationResponse<PublicDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<PublicDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(true)
                .nextCursorId(3L)
                .build();
        given(diaryRepository.search(any(Long.class), any(String.class)))
                .willReturn(expectedResponse);
        // when
        DiaryPaginationResponse<PublicDiaryPreviewResponse> actualResponse = diaryQueryService.search(
                diaryId, keyword);

        // then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.diaries()).hasSize(expectedResponse.diaries().size());
        assertThat(actualResponse.hasNext()).isTrue();
    }

    private List<MyDiaryPreviewResponse> createMyDiaryPreviewResponse() {
        List<MyDiaryPreviewResponse> list = new ArrayList<>();
        list.add(MyDiaryPreviewResponse.from(2L, "testTitle", "PUBLIC", LocalDateTime.now()));
        list.add(MyDiaryPreviewResponse.from(3L, "testTitle2", "PRIVATE",
                LocalDateTime.now().minusHours(1)));
        list.add(MyDiaryPreviewResponse.from(4L, "testTitle3", "PUBLIC",
                LocalDateTime.now().minusHours(2)));
        return list;
    }

    private List<PublicDiaryPreviewResponse> createPublicDiaryPreviewResponse() {
        List<PublicDiaryPreviewResponse> list = new ArrayList<>();
        list.add(PublicDiaryPreviewResponse.from(2L, "testTitle", "testNickname1",
                LocalDateTime.now()));
        list.add(PublicDiaryPreviewResponse.from(3L, "testTitle2", "testNickname2",
                LocalDateTime.now().minusHours(1)));
        list.add(PublicDiaryPreviewResponse.from(4L, "testTitle3", "testNickname3",
                LocalDateTime.now().minusHours(2)));
        return list;
    }

}