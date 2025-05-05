package com.kwakmunsu.diary.diary.controller;

import com.kwakmunsu.diary.diary.controller.dto.DiaryCreateRequest;
import com.kwakmunsu.diary.diary.controller.dto.DiaryUpdateRequest;
import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.global.annotation.CurrentLoginMember;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/my")
    public ResponseEntity<List<MyDiaryPreviewResponse>> readAllMyDiaries(
            @CurrentLoginMember Long memberId) {
        List<MyDiaryPreviewResponse> responses = diaryQueryService.getDiariesByMemberId(memberId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<DiaryDetailResponse> readMyDiary(
            @PathVariable("id") Long diaryId,
            @CurrentLoginMember Long memberId
    ) {
        DiaryDetailResponse response = diaryQueryService.getDiary(diaryId, memberId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long diaryId,
            @CurrentLoginMember Long memberId,
            @Valid @RequestBody DiaryUpdateRequest request
    ) {
        diaryCommandService.update(request.toServiceRequest(diaryId, memberId));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @CurrentLoginMember Long memberId,
            @PathVariable("id") Long diaryId
    ) {
        diaryCommandService.delete(diaryId, memberId);

        return ResponseEntity.noContent().build();
    }

}