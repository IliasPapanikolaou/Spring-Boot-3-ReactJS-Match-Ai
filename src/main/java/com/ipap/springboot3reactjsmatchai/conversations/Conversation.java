package com.ipap.springboot3reactjsmatchai.conversations;

import java.util.List;

public record Conversation(
        String conversationId,
        String profileId,
        List<ChatMessage> messages
) {
}
