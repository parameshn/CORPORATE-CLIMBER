package com.corpclimb.repository;

import com.corpclimb.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmotionScoreRepository extends JpaRepository<EmotionScore, Long> {
    @Query("SELECT es FROM EmotionScore es WHERE es.conversation.id = :conversationId")
    List<EmotionScore> findByConversationId(@Param("conversationId") Long conversationId);

    @Query("SELECT es FROM EmotionScore es WHERE es.conversation.user.id = :userId AND es.conversation.peer.id = :peerId")
    List<EmotionScore> findByUserIdAndPeerId(
            @Param("userId") Long userId,
            @Param("peerId") Long peerId);

    @Query("SELECT es FROM EmotionScore es WHERE es.sentimentScore > :minScore")
    List<EmotionScore> findPositiveSentiments(@Param("minScore") Double minScore);
}