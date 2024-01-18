package com.main.writeRoom.controller;

import com.main.writeRoom.ResponseDTO;
import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.service.TestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/result")
    public ApiResponse<List<ResponseDTO.vocabularyResultDto>> getVocabularyList(@RequestParam(name = "voca")String request) {
        List<ResponseDTO.vocabularyResultDto> response = testService.getVoca(request);
        return ApiResponse.onSuccess(response);
    }
}
