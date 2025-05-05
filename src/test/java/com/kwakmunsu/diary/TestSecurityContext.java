package com.kwakmunsu.diary;

import com.kwakmunsu.diary.global.annotation.TestMember;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Slf4j
public class TestSecurityContext implements WithSecurityContextFactory<TestMember> {

    @Override
    public SecurityContext createSecurityContext(TestMember annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + annotation.role());
        String memberId = String.valueOf(annotation.id());
        log.info("memberId: " + memberId);
        log.info("role: " + annotation.role());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        Collections.singletonList(authority)
                );

        securityContext.setAuthentication(authentication);
        return securityContext;
    }

}