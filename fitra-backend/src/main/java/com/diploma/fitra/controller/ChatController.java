package com.diploma.fitra.controller;

import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.dto.ws.*;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.UserMapper;
import com.diploma.fitra.model.ChatMessage;
import com.diploma.fitra.model.ChatRoom;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.ChatMessageRepository;
import com.diploma.fitra.repo.ChatRoomRepository;
import com.diploma.fitra.repo.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ChatRoomRepository chatRoomRepository;
    ChatMessageRepository chatMessageRepository;
    UserRepository userRepository;
    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<ChatRoomDto> getChatRoomsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return chatRoomRepository.findAllByUser1IdOrUser2Id(user.getId()).stream()
                .map(objects -> {
                    User user1 = ((ChatRoom) objects[0]).getUser1();
                    User user2 = ((ChatRoom) objects[0]).getUser2();
                    UserShortDto userShortDto;
                    if (user1.getId().equals(user.getId())) {
                        userShortDto = UserMapper.INSTANCE.toUserShortDto(user2);
                    } else {
                        userShortDto = UserMapper.INSTANCE.toUserShortDto(user1);
                    }
                    UserShortDto senderDto = UserMapper.INSTANCE.toUserShortDto(((ChatMessage) objects[1]).getSender());
                    return ChatRoomDto.builder()
                            .id(((ChatRoom) objects[0]).getId())
                            .user(userShortDto)
                            .lastMessage(ChatMessageDto.builder()
                                    .id(((ChatMessage) objects[1]).getId())
                                    .sender(senderDto)
                                    .content(((ChatMessage) objects[1]).getContent())
                                    .timestamp(((ChatMessage) objects[1]).getTimestamp())
                                    .build())
                            .unread(chatMessageRepository.countByRoomIdAndRecipientIdAndViewedIsFalse(
                                    ((ChatRoom) objects[0]).getId(), user.getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/rooms/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ChatRoomDto createChatRoom(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Optional<ChatRoom> optChatRoom = chatRoomRepository.findByUser1IdAndUser2Id(user.getId(), userId);
        if (optChatRoom.isPresent()) {
            ChatRoom chatRoom = optChatRoom.get();
            if (chatRoom.getUser1().getId().equals(user.getId())) {
                return ChatRoomDto.builder()
                        .id(chatRoom.getId())
                        .user(UserMapper.INSTANCE.toUserShortDto(chatRoom.getUser2()))
                        .build();
            } else {
                return ChatRoomDto.builder()
                        .id(chatRoom.getId())
                        .user(UserMapper.INSTANCE.toUserShortDto(chatRoom.getUser1()))
                        .build();
            }
        }
        User user2 = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser1(user);
        chatRoom.setUser2(user2);
        chatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .user(UserMapper.INSTANCE.toUserShortDto(chatRoom.getUser2()))
                .build();
    }

    @GetMapping("/rooms/{roomId}/messages")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<ChatMessageDto> getChatMessagesForRoom(@PathVariable UUID roomId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        List<ChatMessage> unreadMessages = new ArrayList<>();
        List<ChatMessageDto> chatMessageDtos = chatMessageRepository.findAllByRoomIdOrderByTimestampAsc(roomId).stream()
                .map(chatMessage -> {
                    if (!chatMessage.isViewed() && chatMessage.getRecipient().getId().equals(user.getId())) {
                        chatMessage.setViewed(true);
                        unreadMessages.add(chatMessage);
                    }
                    return ChatMessageDto.builder()
                            .id(chatMessage.getId())
                            .sender(UserMapper.INSTANCE.toUserShortDto(chatMessage.getSender()))
                            .content(chatMessage.getContent())
                            .timestamp(chatMessage.getTimestamp())
                            .build();
                })
                .collect(Collectors.toList());

        chatMessageRepository.saveAll(unreadMessages);

        messagingTemplate.convertAndSend("/unread-topic/chats/user/" + user.getId(),
                MessagesUnreadDto.builder()
                        .messagesCount(chatMessageRepository.countByRecipientIdAndViewedIsFalse(user.getId()))
                        .build());
        messagingTemplate.convertAndSend("/unread-topic/chat-rooms/" + roomId + "/user/" + user.getId(),
                RoomUpdateDto.builder()
                        .unread(chatMessageRepository.countByRoomIdAndRecipientIdAndViewedIsFalse(roomId, user.getId()))
                        .build());

        return chatMessageDtos;
    }
}
