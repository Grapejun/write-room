package com.main.writeRoom.web.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RoomRequestDTO {

    @Getter
    public static class CreateRoomDTO {
        @NotBlank
        String roomTitle;
    }

    @Getter
    public static class UpdatedRoomInfoDTO {
        @NotBlank
        String roomTitle;
        @NotBlank
        String roomIntroduction;
    }
}
