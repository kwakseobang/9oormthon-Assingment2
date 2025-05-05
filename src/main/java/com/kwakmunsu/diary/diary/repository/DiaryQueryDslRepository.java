package com.kwakmunsu.diary.diary.repository;

import static com.kwakmunsu.diary.diary.entity.QDiary.diary;

import com.kwakmunsu.diary.diary.entity.AccessScope;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.PublicDiaryPreviewResponse;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DiaryQueryDslRepository {

    private final JPAQueryFactory query;

    public Optional<DiaryDetailResponse> findById(Long diaryId) {
        Tuple result = query
                .select(
                        diary.id.as("diaryId"),
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
                        result.get(diary.id.as("diaryId")),
                        result.get(diary.title),
                        result.get(diary.content),
                        result.get(diary.member.nickname),
                        Objects.requireNonNull(result.get(diary.accessScope)).name(),
                        result.get(diary.createdAt)
                ));
    }


    public List<PublicDiaryPreviewResponse> findByPublic() {
        List<Tuple> result = query
                .select(
                        diary.id.as("diaryId"),
                        diary.title,
                        diary.member.nickname,
                        diary.createdAt
                )
                .from(diary)
                .innerJoin(diary.member)
                .where(diary.accessScope.eq(AccessScope.PUBLIC))
                .fetch();

        return result.stream()
                .map(tuple -> PublicDiaryPreviewResponse.from(
                        tuple.get(diary.id.as("diaryId")),
                        tuple.get(diary.title),
                        tuple.get(diary.member.nickname),
                        tuple.get(diary.createdAt)
                )).toList();
    }

}