package com.example.boardinghouse.chat.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long conversationId;
    private Long senderId;
    private String messageText;
    private String imageUrl;
}
