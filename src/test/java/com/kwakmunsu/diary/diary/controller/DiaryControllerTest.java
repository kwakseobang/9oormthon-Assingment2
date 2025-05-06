package com.kwakmunsu.diary.diary.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwakmunsu.diary.TestSecurityConfig;
import com.kwakmunsu.diary.diary.controller.dto.DiaryCreateRequest;
import com.kwakmunsu.diary.diary.controller.dto.DiaryUpdateRequest;
import com.kwakmunsu.diary.diary.service.DiaryCommandService;
import com.kwakmunsu.diary.diary.service.DiaryQueryService;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryDetailResponse;
import com.kwakmunsu.diary.diary.service.dto.response.DiaryPaginationResponse;
import com.kwakmunsu.diary.diary.service.dto.response.my.MyDiaryPreviewResponse;
import com.kwakmunsu.diary.diary.service.dto.response.publicdiary.PublicDiaryPreviewResponse;
import com.kwakmunsu.diary.global.annotation.TestMember;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @DisplayName("자신이 작성한 일기 리스트를 조회한다")
    @TestMember
    @Test
    void readMyDiaries() throws Exception {
        // given
        Long diaryId = 1L;
        List<MyDiaryPreviewResponse> diaries = createMyDiaryPreviewResponse();
        DiaryPaginationResponse<MyDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<MyDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(false)
                .nextCursorId(3L)
                .build();
        given(diaryQueryService.getDiariesByMemberId(diaryId, 1L))
                .willReturn(expectedResponse);
        // then
        // expected
        mockMvc.perform(
                        get(BASE_URL + "/my")
                                .param("lastDiaryId", String.valueOf(diaryId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaries").isArray());
    }

    @DisplayName("자신이 작성한 일기를 상세 조회한다")
    @TestMember
    @Test
    void readMyDiariesDetail() throws Exception {
        // given
        Long diaryId = 1L;
        DiaryDetailResponse expectedResponse = DiaryDetailResponse.from(
                1L,
                "testTitle",
                "testContent",
                "testNickname",
                "PRIVATE",
                LocalDateTime.now()
        );
        given(diaryQueryService.getDiary(any(Long.class), any(Long.class)))
                .willReturn(expectedResponse);
        // expected
        mockMvc.perform(
                        get(BASE_URL + "/my/{id}", diaryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("전채 공개된 일기 리스트를 조회한다")
    @Test
    void readPublicDiary() throws Exception {
        // given
        Long diaryId = 1L;
        List<PublicDiaryPreviewResponse> diaries = createPublicDiaryPreviewResponse();
        DiaryPaginationResponse<PublicDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<PublicDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(false)
                .nextCursorId(3L)
                .build();
        given(diaryQueryService.getDiariesByPublic(any(Long.class)))
                .willReturn(expectedResponse);
        // expected
        mockMvc.perform(
                        get(BASE_URL + "/public")
                                .param("lastDiaryId", String.valueOf(diaryId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaries").isArray())
                .andExpect(jsonPath("$.diaries[0].id").value(2L));
    }

    @DisplayName("전채 공개된 일기를 상세 조회한다")
    @Test
    void readPublicDiaryDetail() throws Exception {
        // given
        Long diaryId = 1L;
        DiaryDetailResponse expectedResponse = DiaryDetailResponse.from(
                1L,
                "testTitle",
                "testContent",
                "testNickname",
                "PUBLIC",
                LocalDateTime.now()
        );
        given(diaryQueryService.getDiaryByAccessScope(any(Long.class)))
                .willReturn(expectedResponse);
        // expected
        mockMvc.perform(
                        get(BASE_URL + "/public/{id}", diaryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("일기 ID가 숫자가 아닌 경우 400을 반환한다 ")
    @Test
    void throwsExceptionWhenParamInvalid() throws Exception {
        // given
        String diaryId = "invalid";

        // expected
        mockMvc.perform(
                        get(BASE_URL + "/public/{id}", diaryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
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
    
    @DisplayName("일기를 검색한다")
    @Test
    void searchDiary() throws Exception {
        Long diaryId = 1L;
        List<PublicDiaryPreviewResponse> diaries = createPublicDiaryPreviewResponse();
        DiaryPaginationResponse<PublicDiaryPreviewResponse> expectedResponse = DiaryPaginationResponse.<PublicDiaryPreviewResponse>builder()
                .diaries(diaries)
                .hasNext(false)
                .nextCursorId(3L)
                .build();
        given(diaryQueryService.search(any(Long.class), any(String.class)))
                .willReturn(expectedResponse);
        // expected
        mockMvc.perform(
                        get(BASE_URL + "/search")
                                .param("keyword", String.valueOf("test"))
                                .param("lastDiaryId", String.valueOf(diaryId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaries").isArray())
                .andExpect(jsonPath("$.diaries[0].id").value(2L));
    }

    private List<MyDiaryPreviewResponse> createMyDiaryPreviewResponse() {
        List<MyDiaryPreviewResponse> list = new ArrayList<>();
        list.add(MyDiaryPreviewResponse.from(2L, "testTitle", "PUBLIC", LocalDateTime.now()));
        list.add(MyDiaryPreviewResponse.from(3L, "testTitle2", "PRIVATE",
                LocalDateTime.now().minusHours(1)));
        list.add(MyDiaryPreviewResponse.from(4L, "testTitle3", "PUBLIC",
                LocalDateTime.now().minusHours(2)));
        return list;
    }

    private List<PublicDiaryPreviewResponse> createPublicDiaryPreviewResponse() {
        List<PublicDiaryPreviewResponse> list = new ArrayList<>();
        list.add(PublicDiaryPreviewResponse.from(2L, "testTitle", "testNickname1",
                LocalDateTime.now()));
        list.add(PublicDiaryPreviewResponse.from(3L, "testTitle2", "testNickname2",
                LocalDateTime.now().minusHours(1)));
        list.add(PublicDiaryPreviewResponse.from(4L, "testTitle3", "testNickname3",
                LocalDateTime.now().minusHours(2)));
        return list;
    }
}