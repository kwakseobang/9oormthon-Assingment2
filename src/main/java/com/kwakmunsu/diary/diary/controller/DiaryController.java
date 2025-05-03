package com.kwakmunsu.diary.diary.controller;

import com.kwakmunsu.diary.diary.controller.dto.DiaryCreateRequest;
import com.kwakmunsu.diary.diary.controller.dto.DiaryUpdateRequest;
import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
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
    public ResponseEntity<List<MyDiaryPreviewResponse>> readAll(@CurrentLoginMember Long memberId) {
        List<MyDiaryPreviewResponse> responses = diaryQueryService.getDiariesByMemberId(memberId);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<Void> update(
            @CurrentLoginMember Long memberId,
            @PathVariable("diaryId") Long diaryId,
            @Valid @RequestBody DiaryUpdateRequest request
    ) {
        diaryCommandService.update(request.toServiceRequest(memberId, diaryId));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> delete(
            @CurrentLoginMember Long memberId,
            @PathVariable("diaryId") Long diaryId
    ) {
        diaryCommandService.delete(diaryId, memberId);

        return ResponseEntity.noContent().build();
    }

}