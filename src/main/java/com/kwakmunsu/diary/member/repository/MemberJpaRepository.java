package com.kwakmunsu.diary.member.repository;

import com.kwakmunsu.diary.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}