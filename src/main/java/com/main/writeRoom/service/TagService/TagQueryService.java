package com.main.writeRoom.service.TagService;

<<<<<<< HEAD
=======
import com.main.writeRoom.domain.Room;
>>>>>>> 87d9c33ad92369211738e13ec5c7303f9c4968f6
import com.main.writeRoom.domain.mapping.NoteTag;
import org.springframework.data.domain.Page;

public interface TagQueryService {
    Page<NoteTag> getTagListForRoom(Long roomId, Integer page);
<<<<<<< HEAD
=======
    Page<NoteTag> findNoteForRoomAndTag(Room room, String tag, Integer page);
>>>>>>> 87d9c33ad92369211738e13ec5c7303f9c4968f6
}
