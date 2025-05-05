package com.kwakmunsu.diary.diary.entity;

import com.kwakmunsu.diary.diary.entity.dto.DiaryUpdateDomainRequest;
import com.kwakmunsu.diary.global.entity.BaseTimeEntity;
import com.kwakmunsu.diary.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "diaries")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Entity
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private AccessScope accessScope;

    @Builder
    private Diary(
            Member member,
            String title,
            String content,
            AccessScope accessScope
    ) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.accessScope = accessScope;
    }

    public void updateDiary(DiaryUpdateDomainRequest request){
        updateTile(request.title());
        updateContent(request.content());
        updateAccessScope(request.accessScope());
    }

    public void updateTile(String newTitle) {
        if (newTitle.equals(this.title)) {
            return;
        }
        this.title = newTitle;
    }

    public void updateContent(String newContent) {
        if (newContent.equals(this.content)) {
            return;
        }
        this.content = newContent;
    }

    public void updateAccessScope(AccessScope newAccessScope) {
        if (accessScope.equals(newAccessScope)) {
            return;
        }
        this.accessScope = newAccessScope;
    }

    public boolean isNotAuthor(Long authorId) {
        return !member.getId().equals(authorId);
    }

}