package com.main.writeRoom.controller;

import com.main.writeRoom.apiPayload.ApiResponse;
import com.main.writeRoom.apiPayload.status.SuccessStatus;
import com.main.writeRoom.config.auth.AuthUser;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.Note;
import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.domain.mapping.RoomParticipation;
import com.main.writeRoom.service.NoteService.NoteQueryServiceImpl;
import com.main.writeRoom.service.RoomService.RoomCommandService;
import com.main.writeRoom.service.SearchService.SearchQueryServiceImpl;
//import com.main.writeRoom.web.dto.topic.TopicResponseDTO;

import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.search.SearchResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.PageRequest.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchQueryServiceImpl searchQueryServiceImpl;
    private final RoomCommandService roomCommandService;

    @GetMapping("/synonyms")
    @Operation(summary = "유사 어휘 검색 API", description = "사용자가 입력한 단어의 동의어를 검색하는 API 입니다. query String 으로 단어를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "voca", description = "사용자가 입력한 단어 입니다!"),
    })
    public ApiResponse<List<SearchResponseDTO.VocabularyResultDTO>> getSynonymList(@RequestParam(name = "voca")String request) {
        List<SearchResponseDTO.VocabularyResultDTO> response = searchQueryServiceImpl.getSynonyms(request);
        return ApiResponse.of(SuccessStatus._OK, response);
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
        List<SearchResponseDTO.VocabularyResultDTO> response = searchQueryServiceImpl.getSimilarKeywords(request);
        return ApiResponse.of(SuccessStatus._OK, response);
    }

    @GetMapping("/topics")
    @Operation(summary = "오늘의 소재 생성 API", description = "'오늘의 소재'를 생성하는 API 이며 새로고침 시에도 사용합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),

    })
    public ApiResponse<List<SearchResponseDTO.VocabularyResultDTO>> getTopicList() {
        List<SearchResponseDTO.VocabularyResultDTO> response = searchQueryServiceImpl.getTopics();
        return ApiResponse.of(SuccessStatus._OK, response);
    }

    @Operation(summary = "노트 검색 API", description = "사용자가 속한 모든 룸의 노트를 검색 하는 API입니다. searchType에는 title, content, tag 를 넣어 노트를 검색할 수 있으며," +
            "그 외의 문자를 넣거나 아무 것도 넣지 않는다면 3가지 모두 검색 조건으로 활용 됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
    })
    @Parameters({
            @Parameter(name = "searchWord", description = "검색어를 입력해 주세요."),
            @Parameter(name = "user", description = "user", hidden = true)
    })
    @GetMapping("/")
    public ApiResponse<NoteResponseDTO.NoteListDTO> searchNotes(
            @AuthUser long userId,
            @RequestParam String searchWord,
            @RequestParam(required = false) String searchType
    )
    {
        List<RoomParticipation> roomParticipationList = roomCommandService.getMyRoomResultList(userId);
        // 일단 유저 아이디로 룸 or 룸 아이디를 전부 불러와야 함. -> 룸참여 데이터 조회 해서 룸 아이디 가져와
        List<Room> roomList = roomParticipationList.stream()
                .map(RoomParticipation::getRoom) // RoomParticipation 객체로부터 Room 객체를 가져옴
                .distinct() // 중복 제거 - 매니저, 참여자 동시 참여 가능
                .toList(); // 결과를 List<Room>으로 수집

        String normalizedSearchWord = "%" + searchWord.toLowerCase() + "%";


        List<Note> noteList = searchQueryServiceImpl.searchNotesInUserRooms(roomList, normalizedSearchWord, searchType);
        // 각각의 룸 아이디로 룸에 들어가서 searchWord를 포함하는 title, contents, tag 가 있는 룸들을 모두 리스트에 저장

        List<NoteResponseDTO.SearchNoteDTO> noteDTOList = NoteConverter.toNoteDTOList(noteList);
        NoteResponseDTO.NoteListDTO noteListDTO = NoteResponseDTO.NoteListDTO.builder()
                .noteList(noteDTOList)
                .build();

        return ApiResponse.of(SuccessStatus._OK, noteListDTO);
    }

}
