package com.diploma.fitra.dto.ws;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomUpdateDto {
    ChatMessageDto lastMessage;
    long unread;
}
