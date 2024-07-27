package com.ipap.springboot3reactjsmatchai.conversations;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Conversation(
        @Id
        String conversationId,
        String profileId,
        List<ChatMessage> messages
) {
}
