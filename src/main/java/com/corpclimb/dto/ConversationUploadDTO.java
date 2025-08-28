package com.corpclimb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationUploadDTO {
    private Long userId;
    private Long peerId;
    private String conversationText;
    private String platform;
    private LocalDateTime conversationDate;
}