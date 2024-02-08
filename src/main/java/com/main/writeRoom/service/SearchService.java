package com.main.writeRoom.service;

//import com.main.writeRoom.web.dto.topic.TopicResponseDTO;
import com.main.writeRoom.web.dto.search.SearchResponseDTO;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private OpenAiService openAiService;
    private static final String MODEL = "gpt-3.5-turbo";

    @Value("${GPT_SECRET}")
    private String apiKey;

    @Transactional
    public List<SearchResponseDTO.VocabularyResultDTO> getSynonyms(String request) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = request + " 에 해당하는 동의어를 5개 이하로 아무런 부가 설명 없이 단어만 알려줘.";

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

    @Transactional
    public List<SearchResponseDTO.VocabularyResultDTO> getSimilarKeywords(String request) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = request + " 와 유사한 키워드 5개를 아무런 부가 설명 없이 단어만 알려줘.";

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

    @Transactional
    public List<SearchResponseDTO.VocabularyResultDTO> getTopics() {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = "글쓰기를 위한 주제 5개를 아무런 부가 설명 없이 단어만 알려줘. 예시: 첫눈이 오면, 공통 되는 부분, 연예인 vs 배우, 코딩 공부란, 애정에 관하여 ";

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

    private List<SearchResponseDTO.VocabularyResultDTO> extractKeywords(String content) {
        List<SearchResponseDTO.VocabularyResultDTO> keywords = new ArrayList<>();

        // 개행 문자로 분할하여 리스트로 변환
        String[] keywordArray = content.split("\\r?\\n");

        for (String keywordTitle : keywordArray) {
            String cleanTitle = keywordTitle.replaceAll("\\d+\\.\\s*", "");
            SearchResponseDTO.VocabularyResultDTO keywordDto = SearchResponseDTO.VocabularyResultDTO.builder().voca(cleanTitle).build();
            keywords.add(keywordDto);
        }
        return keywords;

    }
}
