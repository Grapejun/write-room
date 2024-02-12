package com.main.writeRoom.service.SearchService;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.repository.NoteRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchQueryServiceImpl implements SearchQueryService {

    private final NoteRepository noteRepository;

    private OpenAiService openAiService;
    private static final String MODEL = "gpt-3.5-turbo";

    @Value("${GPT_SECRET}")
    private String apiKey;

    @Transactional
    public List<SearchResponseDTO.VocabularyResultDTO> getTopics() {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = "글쓰기를 위한 주제 5개를 아무런 부가 설명 없이 단어만 알려줘. 예시: 첫눈이 오면, 공통 되는 부분, 연예인 vs 배우, 코딩 공부란, 애정에 관하여";

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
    public List<SearchResponseDTO.VocabularyResultDTO> getSynonyms(String request) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(20));
        String prompt = request + "의 동의어 6개를 쉼표로 구분하여 나열해줘. 예: 스트로베리, Fragaria × ananassa, 딸기열매, 딸기과, 딸기나무, 딸기종자. 추가적인 단어나 문구를 넣지 말고, 동의어만 답변으로 제공해.";
//        예시: 동의어1, 동의어2, 동의어3, 동의어4, 동의어5, 동의어6
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
        String prompt = request + " 와 유사한 키워드 6개를 알려줘. 답변 조건: 반드시 아무런 부가 설명 없이 키워드만 응답해.";
//        예시: 유사어1, 유사어2, 유사어3, 유사어4, 유사어5, 유사어6
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
    public List<SearchResponseDTO.VocabularyResultDTO> extractKeywords(String content) {
        // 모든 키워드를 담을 단일 객체 생성
        SearchResponseDTO.VocabularyResultDTO allKeywordsDto =
                SearchResponseDTO.VocabularyResultDTO.builder().build();

        // 개행 문자로 분할하여 리스트로 변환
        String[] keywordArray = content.split("\\r?\\n");

        // 모든 키워드를 하나의 문자열로 합치기
        String allKeywords = Arrays.stream(keywordArray)
                .map(keyword -> keyword.replaceAll("\\d+\\.\\s*", ""))
                .collect(Collectors.joining(", "));

        // 단일 객체에 모든 키워드 설정
        allKeywordsDto.setVoca(allKeywords);

        // 하나의 객체를 담은 리스트 반환
        List<SearchResponseDTO.VocabularyResultDTO> keywords = new ArrayList<>();
        keywords.add(allKeywordsDto);
        return keywords;
    }

    @Transactional
    public List<Note> searchNotesInUserRooms(List<Room> roomList, String normalizedSearchWord, String searchType) {

        if (searchType == null || searchType.isBlank()) {
            searchType = "default";
        }
        return switch (searchType) {
            case "title" -> noteRepository.findByTitleInUserRooms(roomList, normalizedSearchWord);
            case "content" -> noteRepository.findByContentInUserRooms(roomList, normalizedSearchWord);
            case "tag" -> noteRepository.findByTagInUserRooms(roomList, normalizedSearchWord);
            default -> noteRepository.findByRoomsAndSearchWord(roomList, normalizedSearchWord);
        };
    }
}
