package com.diploma.fitra.dto.ws;

import com.diploma.fitra.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoomDto {
    UUID id;
    UserShortDto user;
    ChatMessageDto lastMessage;
    long unread;
}
