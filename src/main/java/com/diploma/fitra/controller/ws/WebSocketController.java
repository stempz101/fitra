package com.diploma.fitra.controller.ws;

import com.diploma.fitra.dto.ws.*;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.UserMapper;
import com.diploma.fitra.model.ChatMessage;
import com.diploma.fitra.model.ChatRoom;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final InvitationRepository invitationRepository;
    private final JoinRequestRepository requestRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/chat-room/{roomId}")
    public ChatMessageDto sendChatMessage(@DestinationVariable UUID roomId,
                                          @Payload ChatMessageSaveDto chatMessageSaveDto,
                                          Principal principal) {
        User sender = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!chatMessageSaveDto.getSenderId().equals(sender.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(RuntimeException::new);
        User recipient = userRepository.findById(chatMessageSaveDto.getRecipientId())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setRoom(chatRoom);
        chatMessage.setContent(chatMessageSaveDto.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage = chatMessageRepository.save(chatMessage);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .id(chatMessage.getId())
                .sender(UserMapper.INSTANCE.toUserShortDto(sender))
                .recipient(UserMapper.INSTANCE.toUserShortDto(recipient))
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .build();

        messagingTemplate.convertAndSend("/unread-topic/chats/user/" + recipient.getId(),
                MessagesUnreadDto.builder()
                        .messagesCount(chatMessageRepository.countByRecipientIdAndViewedIsFalse(recipient.getId()))
                        .build());
        messagingTemplate.convertAndSend("/unread-topic/chat-rooms/" + roomId + "/user/" + recipient.getId(),
                RoomUpdateDto.builder()
                        .lastMessage(chatMessageDto)
                        .unread(chatMessageRepository.countByRoomIdAndRecipientIdAndViewedIsFalse(roomId, recipient.getId()))
                        .build());
        return chatMessageDto;
    }

    @MessageMapping("/chats/user/{userId}")
    @SendTo("/unread-topic/chats/user/{userId}")
    public MessagesUnreadDto getUnreadMessagesCount(@DestinationVariable Long userId,
                                                    @Payload MessagesUnreadDto messagesUnreadDto,
                                                    Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        long count = chatMessageRepository.countByRecipientIdAndViewedIsFalse(userId);
        messagesUnreadDto.setMessagesCount(count);
        return messagesUnreadDto;
    }

    @MessageMapping("/invitations/user/{userId}")
    @SendTo("/unread-topic/invitations/user/{userId}")
    public InvitationsUnreadDto getUnreadInvitationsCount(@DestinationVariable Long userId,
                                                          @Payload InvitationsUnreadDto invitationsUnreadDto,
                                                          Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        long count = invitationRepository.countByUserIdAndViewedIsFalse(userId);
        invitationsUnreadDto.setInvitationsCount(count);
        return invitationsUnreadDto;
    }

    @MessageMapping("/user/{userId}/travels/requests")
    @SendTo("/unread-topic/user/{userId}/travels/requests")
    public RequestsUnreadDto getUnreadUserTravelsRequestsCount(@DestinationVariable Long userId,
                                                               @Payload RequestsUnreadDto requestsUnreadDto,
                                                               Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        long count = requestRepository.countByTravel_CreatorIdAndViewedIsFalse(userId);
        requestsUnreadDto.setRequestsCount(count);
        return requestsUnreadDto;
    }

    @MessageMapping("/travel/{travelId}/requests")
    @SendTo("/unread-topic/travel/{travelId}/requests")
    public RequestsUnreadDto getUnreadTravelRequestsCount(@DestinationVariable Long travelId,
                                                          @Payload RequestsUnreadDto requestsUnreadDto,
                                                          Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        long count = requestRepository.countByTravelIdAndViewedIsFalse(travelId);
        requestsUnreadDto.setRequestsCount(count);
        return requestsUnreadDto;
    }
}
