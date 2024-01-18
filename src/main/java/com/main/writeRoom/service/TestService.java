package com.main.writeRoom.service;

import com.main.writeRoom.ResponseDTO;
import com.main.writeRoom.ResponseDTO.vocabularyResultDto;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {
    private OpenAiService openAiService;
    private static final String MODEL = "gpt-3.5-turbo";

    @Value("${GPT_SECRET}")
    private String apiKey;

    @Transactional
    public List<ResponseDTO.vocabularyResultDto> getVoca(String request) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = request + " 에 해당하는 유사 키워드 5개만 부가 설명 없이 키워드(단어)만 알려줘.";

        ChatCompletionRequest requester = ChatCompletionRequest.builder()
                .model(MODEL)
                .temperature(0.8)
                .messages(List.of(
                        new ChatMessage("user", prompt)
                )).build();

        ChatCompletionChoice chatCompletionResult = openAiService.createChatCompletion(requester).getChoices().get(0);

        String contentResult = chatCompletionResult.getMessage().getContent();
        return extractKeywords(contentResult);
    }

    private List<ResponseDTO.vocabularyResultDto> extractKeywords(String content) {
        List<ResponseDTO.vocabularyResultDto> keywords = new ArrayList<>();

        // 개행 문자로 분할하여 리스트로 변환
        String[] keywordArray = content.split("\\r?\\n");

        for (String keywordTitle : keywordArray) {
            String cleanTitle = keywordTitle.replaceAll("\\d+\\.\\s*", "");
            vocabularyResultDto keywordDto = vocabularyResultDto.builder().voca(cleanTitle).build();
            keywords.add(keywordDto);
        }
        return keywords;
    }
}
