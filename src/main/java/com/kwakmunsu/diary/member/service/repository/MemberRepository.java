package com.kwakmunsu.diary.member.service.repository;

import com.kwakmunsu.diary.member.entity.Member;

public interface MemberRepository {

    Long create(Member member);

    Member findByEmail(String email);

    Member findById(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Member findByRefreshToken(String refreshToken);

}