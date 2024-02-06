package com.main.writeRoom.converter;

import com.main.writeRoom.domain.Tag;

import java.util.List;
import java.util.stream.Collectors;

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
}
