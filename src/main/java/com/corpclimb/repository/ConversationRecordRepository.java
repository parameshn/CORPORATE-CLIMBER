package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationRecordRepository extends JpaRepository<ConversationRecord, Long> {
    @Query("SELECT cr FROM ConversationRecord cr WHERE cr.user.id = :userId AND cr.peer.id = :peerId")
    List<ConversationRecord> findByUserIdAndPeerId(
            @Param("userId") Long userId,
            @Param("peerId") Long peerId);

    @Query("SELECT cr FROM ConversationRecord cr WHERE cr.user.id = :userId AND cr.conversationDate >= :fromDate")
    List<ConversationRecord> findByUserIdAndConversationDateAfter(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDateTime fromDate);

    @Query("SELECT cr FROM ConversationRecord cr WHERE cr.processed = false")
    List<ConversationRecord> findUnprocessedConversations();

    long countByUserIdAndPeerId(Long userId, Long peerId);

    Optional<ConversationRecord> findTopByUserIdAndPeerIdOrderByConversationDateDesc(Long userId, Long peerId);
}


   

