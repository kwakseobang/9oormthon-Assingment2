package com.kwakmunsu.diary.auth.controller;

import com.kwakmunsu.diary.auth.service.AuthCommandService;
import com.kwakmunsu.diary.auth.service.AuthQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;

}