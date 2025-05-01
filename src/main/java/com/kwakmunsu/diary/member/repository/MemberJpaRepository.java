package com.kwakmunsu.diary.member.repository;

import com.kwakmunsu.diary.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByRefreshToken(String refreshToken);

}