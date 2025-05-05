package com.kwakmunsu.diary.diary.repository;

import static com.kwakmunsu.diary.diary.entity.QDiary.diary;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class DiaryQueryDslRepository {

    private final JPAQueryFactory query;
    private static final int PAGE_SIZE = 10;

    public Optional<DiaryDetailResponse> findById(Long diaryId) {
        Tuple result = query
                .select(
                        diary.id,
                        diary.title,
                        diary.content,
                        diary.member.nickname,
                        diary.accessScope,
                        diary.createdAt
                )
                .from(diary)
                .innerJoin(diary.member)
                .where(diary.id.eq(diaryId))
                .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                DiaryDetailResponse.from(
                        result.get(diary.id),
                        result.get(diary.title),
                        result.get(diary.content),
                        result.get(diary.member.nickname),
                        Objects.requireNonNull(result.get(diary.accessScope)).name(),
                        result.get(diary.createdAt)
                ));
    }

    public DiaryPaginationResponse<PublicDiaryPreviewResponse> findByPublic(Long cursorId) {
        List<Tuple> result = query
                .select(
                        diary.id,
                        diary.title,
                        diary.member.nickname,
                        diary.createdAt
                )
                .from(diary)
                .innerJoin(diary.member)
                .where(
                        diary.accessScope.eq(AccessScope.PUBLIC),
                        cursorIdCondition(cursorId)
                )
                .orderBy(diary.id.desc())
                .limit(PAGE_SIZE + 1)
                .fetch();

        return getPublicDiaryPreviewPaginationResponse(result);
    }

    public DiaryPaginationResponse<PublicDiaryPreviewResponse> searchPublicDiaries(
            String keyword,
            Long diaryId
    ) {
        List<Tuple> result = query
                .select(
                        diary.id,
                        diary.title,
                        diary.member.nickname,
                        diary.createdAt
                )
                .from(diary)
                .innerJoin(diary.member)
                .where(
                        diary.accessScope.eq(AccessScope.PUBLIC),
                        cursorIdCondition(diaryId),
                        keywordCondition(keyword)
                )
                .orderBy(diary.id.desc())
                .limit(PAGE_SIZE + 1)
                .fetch();

        return getPublicDiaryPreviewPaginationResponse(result);
    }

    private DiaryPaginationResponse<PublicDiaryPreviewResponse> getPublicDiaryPreviewPaginationResponse(
            List<Tuple> result
    ) {
        boolean hasNext = result.size() > PAGE_SIZE;

        List<Tuple> content = getTuples(result, hasNext);

        List<PublicDiaryPreviewResponse> diaries = content.stream()
                .map(tuple -> PublicDiaryPreviewResponse.from(
                        tuple.get(diary.id),
                        tuple.get(diary.title),
                        tuple.get(diary.member.nickname),
                        tuple.get(diary.createdAt)
                )).toList();

        Long nextCursorId = getNextCursorIdOrNull(hasNext, diaries);

        return DiaryPaginationResponse.<PublicDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(hasNext)
                .nextCursorId(nextCursorId)
                .build();
    }

    private BooleanExpression keywordCondition(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return diary.title.containsIgnoreCase(keyword).or
                    (diary.content.containsIgnoreCase(keyword));
        }
        return null;
    }

    public DiaryPaginationResponse<MyDiaryPreviewResponse> findDiariesByMemberId(
            Long diaryId,
            Long memberId
    ) {
        List<Tuple> result = query
                .select(
                        diary.id,
                        diary.title,
                        diary.accessScope,
                        diary.createdAt
                )
                .from(diary)
                .where(
                        diary.member.id.eq(memberId),
                        cursorIdCondition(diaryId)
                )
                .orderBy(diary.id.desc())
                .limit(PAGE_SIZE + 1)
                .fetch();

        boolean hasNext = result.size() > PAGE_SIZE;

        List<Tuple> content = getTuples(result, hasNext);

        List<MyDiaryPreviewResponse> diaries = content.stream()
                .map(tuple -> MyDiaryPreviewResponse.from(
                        tuple.get(diary.id),
                        tuple.get(diary.title),
                        Objects.requireNonNull(tuple.get(diary.accessScope)).name(),
                        tuple.get(diary.createdAt)
                )).toList();

        Long nextCursorId = getNextCursorIdOrNull(hasNext, diaries);

        return DiaryPaginationResponse.<MyDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(hasNext)
                .nextCursorId(nextCursorId)
                .build();
    }
    private <T extends DiaryPreviewResponse> Long getNextCursorIdOrNull(
            boolean hasNext,
            List<T> diaries
    ) {
        if (hasNext && !diaries.isEmpty()) {
            T lastDiary = diaries.get(diaries.size() - 1);
            return lastDiary.id();
        }
        return null;
    }

    // + 1로 다음이 있는 지 확인했기에 있다면 PAGE_SIZE 만큼만 내려줘야함

    private List<Tuple> getTuples(List<Tuple> result, boolean hasNext) {
        if (hasNext) {
            return result.subList(0, PAGE_SIZE);
        }
        return result;
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return diary.id.lt(cursorId); // ID가 커서보다 작은 항목 조회 (내림차순 정렬 시)
    }

}