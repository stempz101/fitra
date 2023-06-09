package com.diploma.fitra.repo;

import com.diploma.fitra.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findAllByRoomIdOrderByTimestampAsc(UUID roomId);

    long countByRecipientIdAndViewedIsFalse(Long recipientId);

    long countByRoomIdAndRecipientIdAndViewedIsFalse(UUID roomId, Long recipientId);
}
