package com.diploma.fitra.dto.ws;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageSaveDto {
    Long senderId;
    Long recipientId;
    String content;
}
