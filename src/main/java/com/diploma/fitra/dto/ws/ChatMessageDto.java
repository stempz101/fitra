package com.diploma.fitra.dto.ws;

import com.diploma.fitra.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDto {
    UUID id;
    UserShortDto sender;
    UserShortDto recipient;
    String content;
    LocalDateTime timestamp;
}
