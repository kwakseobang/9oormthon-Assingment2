package com.kwakmunsu.diary.global.config;


import com.kwakmunsu.diary.global.annotation.CurrentLoginMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@TestConfiguration
public
class WebMvcConfigTest implements WebMvcConfigurer {

    private final CurrentLoginMemberArgumentResolver currentLoginMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentLoginMemberArgumentResolver);
    }

}