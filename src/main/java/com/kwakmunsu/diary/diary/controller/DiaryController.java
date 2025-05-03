package com.kwakmunsu.diary.diary.controller;

import com.kwakmunsu.diary.diary.controller.dto.DiaryCreateRequest;
import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
import com.kwakmunsu.diary.global.annotation.CurrentLoginMember;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/diaries")
@RestController
public class DiaryController {

    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;


    @PostMapping
    public ResponseEntity<Void> create(
            @CurrentLoginMember Long memberId,
            @Valid @RequestBody DiaryCreateRequest request
    ) {
        Long diaryId = diaryCommandService.create(request.toServiceRequest(memberId));
        URI location = URI.create("/diaries/" + diaryId);

        return ResponseEntity.created(location).build();
    }

}