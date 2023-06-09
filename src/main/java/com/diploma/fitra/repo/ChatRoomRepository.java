package com.diploma.fitra.repo;

import com.diploma.fitra.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query("select cr, cm from _chat_rooms cr " +
            "left join _chat_messages cm on cr.id = cm.room.id " +
            "and cm.timestamp = (select max(m.timestamp) from _chat_messages m where m.room.id = cr.id) " +
            "where (cr.user1.id = ?1 or cr.user2.id = ?1) and cm is not null")
    List<Object[]> findAllByUser1IdOrUser2Id(Long userId);

    @Query("select cr from _chat_rooms cr where (cr.user1.id = ?1 and cr.user2.id = ?2) or (cr.user1.id = ?2 and cr.user2.id = ?1)")
    Optional<ChatRoom> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
