package com.main.writeRoom.service.NoteService;

import com.main.writeRoom.apiPayload.status.ErrorStatus;
import com.main.writeRoom.converter.NoteConverter;
import com.main.writeRoom.domain.*;
import com.main.writeRoom.domain.User.User;
import com.main.writeRoom.domain.mapping.EmojiClick;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.handler.NoteHandler;
import com.main.writeRoom.repository.NoteRepository;

import java.util.*;

import com.main.writeRoom.web.dto.note.NoteResponseDTO;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteQueryServiceImpl implements NoteQueryService{
    private final NoteRepository noteRepository;

    public List<Note> findNoteForCategoryAndRoom(Category category, Room room) {
        return noteRepository.findAllByCategoryAndRoom(category, room);
    }

    public Page<Note> getNoteListForRoom(Room room, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoom(room, pageRequest);
    }

    public Note findNote(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteHandler(ErrorStatus.NOTE_NOT_FOUND));
    }

    @Transactional
    public NoteResponseDTO.NoteResult getNote(Note note, List<EmojiClick> emojiClickList) {

        ArrayList<Integer> emojiCountList = new ArrayList<>(Collections.nCopies(6, 0));


        // emojiClickList를 순회하면서 각 이모지의 개수를 계산.
        emojiClickList.forEach(emojiClick -> {
            Emoji emoji = emojiClick.getEmoji();
            if (emoji.getEmojiNum() >= 1 && emoji.getEmojiNum() <= 6) {
                long index = emoji.getEmojiNum() - 1; // 리스트의 인덱스는 0부터 시작
                emojiCountList.set((int)index, emojiCountList.get((int)index) + 1); // 카운트 증가
            }
        });

        NoteResponseDTO.NoteResult noteResult = NoteConverter.toNoteResponseDTO(note, emojiCountList);

        // NoteTag 리스트에서 Tag 객체를 가져오고, Note 객체의 참조를 제거하여 순환 참조 방지
        List<TagResponseDTO.TagList> tagDTOList = note.getNoteTagList().stream()
                .map(NoteTag::getTag)
                .filter(Objects::nonNull)
                .map(tag -> new TagResponseDTO.TagList(tag.getId(), tag.getContent())) // TagDTO는 순환 참조가 없는 DTO
                .toList();

        noteResult.getTagList().addAll(tagDTOList);

        return noteResult;
    }

    public Page<Note> findNoteForRoomAndCategory(Category category, Room room, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoomAndCategory(room, category, pageRequest);
    }

    public Page<Note> findNoteListForRoomAndUser(Room room, User user, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteRepository.findAllByRoomAndUser(room, user, pageRequest);
    }
}