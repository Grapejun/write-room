package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.service.SearchService;
//import com.main.writeRoom.web.dto.topic.TopicResponseDTO;

import com.main.writeRoom.web.dto.search.SearchResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/synonyms")
    @Operation(summary = "유사 어휘 검색 API", description = "사용자가 입력한 단어의 동의어를 검색하는 API 입니다. query String 으로 단어를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "voca", description = "사용자가 입력한 단어 입니다!"),
    })
    public ApiResponse<List<SearchResponseDTO.VocabularyResultDTO>> getSynonymList(@RequestParam(name = "voca")String request) {
        List<SearchResponseDTO.VocabularyResultDTO> response = searchService.getSynonyms(request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/similarKeywords")
    @Operation(summary = "유사 키워드 검색 API", description = "사용자가 입력한 단어와 유사한 키워드를 검색하는 API 이며 5개의 단어를 보여 줍니다. query String 으로 단어를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "voca", description = "사용자가 입력한 단어 입니다!"),
    })
    public ApiResponse<List<SearchResponseDTO.VocabularyResultDTO>> getVocabularyList(@RequestParam(name = "voca")String request) {
        List<SearchResponseDTO.VocabularyResultDTO> response = searchService.getSimilarKeywords(request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/topics")
    @Operation(summary = "오늘의 소재 생성 API", description = "'오늘의 소재'를 생성하는 API 이며 새로고침 시에도 사용합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),

    })
    public ApiResponse<List<SearchResponseDTO.VocabularyResultDTO>> getTopicList() {
        List<SearchResponseDTO.VocabularyResultDTO> response = searchService.getTopics();
        return ApiResponse.onSuccess(response);
    }

}
