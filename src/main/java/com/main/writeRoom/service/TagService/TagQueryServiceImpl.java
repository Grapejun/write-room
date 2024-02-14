package com.main.writeRoom.service.TagService;

import com.main.writeRoom.domain.Room;
import com.main.writeRoom.domain.Tag;
import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.repository.NoteTagRepository;
<<<<<<< HEAD
=======
import com.main.writeRoom.repository.TagRepository;
>>>>>>> 87d9c33ad92369211738e13ec5c7303f9c4968f6
import com.main.writeRoom.service.RoomService.RoomQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagQueryServiceImpl implements TagQueryService {
    private final NoteTagRepository noteTagRepository;
    private final RoomQueryService roomQueryService;
<<<<<<< HEAD
=======
    private final TagRepository tagRepository;
>>>>>>> 87d9c33ad92369211738e13ec5c7303f9c4968f6

    public Page<NoteTag> getTagListForRoom(Long roomId, Integer page) {
        Room room = roomQueryService.findRoom(roomId);
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteTagRepository.findAllByRoom(room, pageRequest);
    }
<<<<<<< HEAD
=======

    public Page<NoteTag> findNoteForRoomAndTag(Room room, String tagContent, Integer page) {
        Tag tag = tagRepository.findByContent(tagContent);
        PageRequest pageRequest = PageRequest.of(page, 10);
        return noteTagRepository.findAllByRoomAndTag(room, tag, pageRequest);
    }
>>>>>>> 87d9c33ad92369211738e13ec5c7303f9c4968f6
}
