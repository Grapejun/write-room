package com.main.writeRoom.service.SearchService;

import com.main.writeRoom.web.dto.search.SearchResponseDTO;

import java.util.List;

public interface SearchQueryService {

    List<SearchResponseDTO.VocabularyResultDTO> getSynonyms(String request);

    List<SearchResponseDTO.VocabularyResultDTO> getSimilarKeywords(String request);

    List<SearchResponseDTO.VocabularyResultDTO> getTopics();
}
