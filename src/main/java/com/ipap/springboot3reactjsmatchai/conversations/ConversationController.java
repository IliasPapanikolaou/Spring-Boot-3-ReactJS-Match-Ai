package com.ipap.springboot3reactjsmatchai.conversations;

import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;
    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<Conversation> createNewConversation(@RequestBody CreateConversationRequest request) {
        // Search if profile exists
        profileRepository.findById(request.profileId)
                .orElseThrow(() -> {
                    log.warn("Profile with id {} not found", request.profileId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Profile with id " + request.profileId + " not found");
                });

        // Continue creating conversation
        Conversation conversation = new Conversation(UUID.randomUUID().toString(),
                request.profileId, Collections.emptyList());

        Conversation createdConversation = conversationRepository.save(conversation);
        log.info("Created conversation: {}", createdConversation);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdConversation);
    }

    @PutMapping("/{conversationId}")
    public ResponseEntity<Conversation> addMessageToConversation(
            @PathVariable String conversationId,
            @RequestBody ChatMessage chatMessage) {

        // Search conversation by conversationId
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> {
            log.warn("Could not find conversation with id: {}", conversationId);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unable to find conversation with id " + conversationId);
        });

        // Search for match profileId
        String matchProfileId = conversation.profileId();
        Profile matchProfile = profileRepository.findById(matchProfileId).orElseThrow(() -> {
            log.warn("Profile with profile id {} not found", chatMessage.authorId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unable to find profile with id " + chatMessage.authorId());
        });

        // Search if chatMessage authorId is valid
        Profile userProfile = profileRepository.findById(chatMessage.authorId()).orElseThrow(() -> {
            log.warn("Profile with author id {} not found", chatMessage.authorId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unable to find author with id " + chatMessage.authorId());
        });

        // TODO: Need to validate that the author of a message happens to be only the profile associated with the message user

        // Add message to conversation
        ChatMessage chatMessageWithTime = new ChatMessage(
                chatMessage.authorId(),
                chatMessage.messageText(),
                LocalDateTime.now());

        conversation.messages().add(chatMessageWithTime);

        // Add response message from LLM
        Conversation conversationWithAiResponse = conversationService.generateProfleResponseConversation(
                conversation, matchProfile, userProfile);

        // Persist conversation
        Conversation savedConversation = conversationRepository.save(conversationWithAiResponse);

        return new ResponseEntity<>(savedConversation, HttpStatus.OK);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> {
            log.warn("Conversation with id {} not found", conversationId);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Conversation with id " + conversationId + " not found");
        });
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    public record CreateConversationRequest (
            String profileId
    ) {}

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversationById(@PathVariable String conversationId) {
        conversationRepository.deleteById(conversationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
