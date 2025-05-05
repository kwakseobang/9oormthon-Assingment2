package com.kwakmunsu.diary.global.annotation;

import com.kwakmunsu.diary.TestSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContext.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface TestMember {

    long id() default 1L;

    String role() default "MEMBER";

}