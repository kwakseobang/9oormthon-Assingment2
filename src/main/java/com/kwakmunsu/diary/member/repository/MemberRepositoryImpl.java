package com.kwakmunsu.diary.member.repository;

import com.kwakmunsu.diary.global.exception.DiaryNotFoundException;
import com.kwakmunsu.diary.global.exception.dto.ErrorMessage;
import com.kwakmunsu.diary.member.entity.Member;
import com.kwakmunsu.diary.member.service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Long create(Member member) {
        return memberJpaRepository.save(member).getId();
    }

    @Override
    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new DiaryNotFoundException(
                        ErrorMessage.NOT_FOUND_MEMBER.getMessage())
                );
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

}