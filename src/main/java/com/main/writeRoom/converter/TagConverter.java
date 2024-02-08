package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Tag;

import com.main.writeRoom.domain.mapping.NoteTag;
import com.main.writeRoom.web.dto.tag.TagResponseDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class TagConverter {
    public static List<Tag> toTagList(List<String> tagStringList) {
        return tagStringList.stream()
                .map(tag ->
                        Tag.builder()
                                .content(tag)
                                .build()
                ).collect(Collectors.toList());
    }

    // 노트태그 리스트를 태그 리스트로 바꿔야 함.

    public static TagResponseDTO.tagListForRoom toTagListForRoom(Page<NoteTag> noteTags, Long roomId) {
        Set<Long> uniqueTagIds = new HashSet<>();
        List<TagResponseDTO.tagListForRoomList> toTagListForRoomList = noteTags.stream()
                .filter(tag -> uniqueTagIds.add(tag.getTag().getId()))
                .map(tag -> tagListForRoomInfoDTO(tag.getTag()))
                .collect(Collectors.toList());

        return TagResponseDTO.tagListForRoom.builder()
                .roomId(roomId)
                .isFirst(noteTags.isFirst())
                .isLast(noteTags.isLast())
                .totalPage(noteTags.getTotalPages())
                .totalElements(noteTags.getTotalElements())
                .listSize(noteTags.getSize())
                .tagListForRoomList(toTagListForRoomList)
                .build();
    }

    public static TagResponseDTO.tagListForRoomList tagListForRoomInfoDTO(Tag tag) {
        return TagResponseDTO.tagListForRoomList.builder()
                .tagId(tag.getId())
                .tagName(tag.getContent())
                .build();
    }
}
