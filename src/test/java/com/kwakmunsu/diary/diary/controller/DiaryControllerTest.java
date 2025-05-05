package com.kwakmunsu.diary.diary.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.auth.controller.dto.MemberCreateRequest;
import com.kwakmunsu.diary.diary.controller.dto.DiaryCreateRequest;
import com.kwakmunsu.diary.diary.controller.dto.DiaryUpdateRequest;
import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
import com.kwakmunsu.diary.global.annotation.TestMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    private static final String BASE_URL = "/diaries";

    @MockBean
    private DiaryCommandService diaryCommandService;

    @MockBean
    private DiaryQueryService diaryQueryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("일기를 생성한다")
    @TestMember
    @Test
    void createDiary() throws Exception {
        // given
        String testTitle = "testTitle";
        String testContent = "testContent";
        Long testMemberId = 1L;
        Long testDiaryId = 1L;
        DiaryCreateRequest request = DiaryCreateRequest.builder()
                .title(testTitle)
                .content(testContent)
                .accessScope("PUBLIC")
                .build();
        given(diaryCommandService.create(request.toServiceRequest(testMemberId)))
                .willReturn(testDiaryId);

        // expected
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/diaries/" + testDiaryId));
    }

    @DisplayName("일기를 수정한다")
    @TestMember
    @Test
    void updateDiary() throws Exception {
        // given
        String testTitle = "testTitle";
        String testContent = "testContent";
        Long testDiaryId = 1L;
        DiaryUpdateRequest request = DiaryUpdateRequest.builder()
                .title(testTitle)
                .content(testContent)
                .accessScope("PRIVATE")
                .build();

        // expected
        mockMvc.perform(
                        patch(BASE_URL + "/{id}", testDiaryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(diaryCommandService).update(any());
    }

    @DisplayName("일기를 삭제한다")
    @TestMember
    @Test
    void deleteDiary() throws Exception {
        // given
        Long testDiaryId = 1L;

        // expected
        mockMvc.perform(
                        delete(BASE_URL + "/{id}", testDiaryId)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(diaryCommandService).delete(any(), any());
    }

    @DisplayName("일기 생성 및 수정 요청 값 유효성 테스트")
    @TestMember
    @ParameterizedTest
    @CsvSource({
            ", testContent, PUBLIC, 일기 제목을 입력해주세요",
            "testTitle, ,PUBLIC, 일기 내용을 입력해주세요",
            "testTitle, testContent, , PUBLIC 또는 PRIVATE 를 정확히 입력해주세요",
            "testTitle, testContent, PUsd, PUBLIC 또는 PRIVATE 를 정확히 입력해주세요",
            "testTitle, testContent, private, PUBLIC 또는 PRIVATE 를 정확히 입력해주세요",
            "testTitle, testContent, public, PUBLIC 또는 PRIVATE 를 정확히 입력해주세요",
    })
    void shouldFailValidationDiaryCreateAndUpdate(
            String title,
            String content,
            String accessScope,
            String expectedMessage
    ) throws Exception {
        // given
        DiaryCreateRequest request = DiaryCreateRequest.builder()
                .title(title)
                .content(content)
                .accessScope(accessScope)
                .build();
        // expected
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.*").value(expectedMessage));
    }

}