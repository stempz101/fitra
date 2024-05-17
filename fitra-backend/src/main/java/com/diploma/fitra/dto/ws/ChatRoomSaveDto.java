package com.diploma.fitra.dto.ws;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomSaveDto {
    private Long user1Id;
    private Long user2Id;
}
