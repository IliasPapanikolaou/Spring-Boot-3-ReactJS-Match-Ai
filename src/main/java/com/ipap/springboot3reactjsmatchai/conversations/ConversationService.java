package com.ipap.springboot3reactjsmatchai.conversations;

import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final OllamaChatModel chatModel;

    public Conversation generateProfleResponseConversation(
            Conversation conversation, Profile matchProfile, Profile userProfile) {

        // System message
        String contextPrompt = STR."""
                You are a \{matchProfile.age()} year old \{matchProfile.ethnicity()} \{matchProfile.gender()}
                called \{matchProfile.firstName()} \{matchProfile.lastName()} matched
                with a \{userProfile.age()} year old \{userProfile.ethnicity()} \{userProfile.gender()}
                with interests: \{userProfile.bio()} called
                \{userProfile.firstName()} \{userProfile.lastName()} on a date application.
                This is an in-app text conversation between you two.
                Pretend to be the provided person and respond to the conversation as if writing on a dating application.
                Your bio is: \{matchProfile.bio()} and your Myers Briggs personality type is
                \{matchProfile.myersBriggsPersonalityType()}.
                Respond in the role of this person only. Keep responses brief.
                """;

        SystemMessage systemMessage = new SystemMessage(contextPrompt);

        // Map messages to either Ai Assistant or User messages
        List<AbstractMessage> conversationMessages = conversation.messages().stream().map(message -> {
            if (message.authorId().equals(matchProfile.id())) {
                return new AssistantMessage(message.messageText());
            } else {
                return new UserMessage(message.messageText());
            }
        }).toList();

        // Add system message and rest of conversation messages in one list
        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(conversationMessages);

        // Call LLM
        Prompt prompt = new Prompt(allMessages);
        ChatResponse response = chatModel.call(prompt);

        // Create new ChatMessage
        ChatMessage chatMessage = new ChatMessage(
                matchProfile.id(),
                response.getResult().getOutput().getContent(),
                LocalDateTime.now());

        // Add ChatMessage to conversation
        conversation.messages().add(chatMessage);

        return conversation;
    }

}
