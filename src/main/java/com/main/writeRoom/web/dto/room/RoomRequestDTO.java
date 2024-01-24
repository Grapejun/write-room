package com.main.writeRoom.web.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class RoomRequestDTO {

    @Getter
    public static class CreateRoomDTO {
        @NotBlank
        String roomTitle;
        @NotBlank
        String roomContent;
    }
}
