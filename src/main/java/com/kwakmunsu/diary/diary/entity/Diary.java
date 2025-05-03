package com.kwakmunsu.diary.diary.entity;

import com.kwakmunsu.diary.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private AccessLevel accessLevel;

    @Builder
    private Diary(
            Long memberId,
            String title,
            String content,
            AccessLevel accessLevel
    ) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.accessLevel = accessLevel;
    }

}