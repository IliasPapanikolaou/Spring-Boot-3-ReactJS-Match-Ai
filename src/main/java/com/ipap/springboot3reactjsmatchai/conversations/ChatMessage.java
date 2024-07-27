package com.ipap.springboot3reactjsmatchai.conversations;

import java.time.LocalDateTime;

public record ChatMessage(
        String authorId,
        String messageText,
        LocalDateTime messageTime
) {
}
