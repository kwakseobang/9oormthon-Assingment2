package com.kwakmunsu.diary.diary.controller;

import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/diaries")
@RestController
public class DiaryController {

    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;

}