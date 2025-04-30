package com.kwakmunsu.diary.auth.controller;

import com.kwakmunsu.diary.auth.controller.dto.MemberCreateRequest;
import com.kwakmunsu.diary.auth.service.AuthCommandService;
import com.kwakmunsu.diary.auth.service.AuthQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;


    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        Long memberId = authCommandService.signUp(request.toServiceRequest());
        URI location = URI.create("/members/" + memberId);

        return ResponseEntity.created(location).build();
    }

}