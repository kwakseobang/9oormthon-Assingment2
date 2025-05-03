package com.kwakmunsu.diary.global.annotation;

import com.kwakmunsu.diary.TestSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContext.class)
public @interface TestMember {

    long id() default 1L;

    String roles() default "MEMBER";

}