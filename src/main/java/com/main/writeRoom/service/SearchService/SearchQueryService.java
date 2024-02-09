package com.main.writeRoom.service.SearchService;

import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.web.dto.search.SearchResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchQueryService {

    List<SearchResponseDTO.VocabularyResultDTO> getSynonyms(String request);

    List<SearchResponseDTO.VocabularyResultDTO> getSimilarKeywords(String request);

    List<SearchResponseDTO.VocabularyResultDTO> getTopics();

    List<Note> searchNotesInUserRooms(List<Room> userRooms, String searchWord);
}
