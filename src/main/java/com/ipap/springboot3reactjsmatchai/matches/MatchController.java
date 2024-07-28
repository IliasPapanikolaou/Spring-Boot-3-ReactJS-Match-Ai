package com.ipap.springboot3reactjsmatchai.matches;

import com.ipap.springboot3reactjsmatchai.conversations.Conversation;
import com.ipap.springboot3reactjsmatchai.conversations.ConversationRepository;
import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;
    private final MatchRepository matchRepository;

    @PostMapping
    public ResponseEntity<Match> createNewMatch(@RequestBody CreateMatchRequest createMatchRequest) {
        // Search if profile exists
        Profile profile = profileRepository.findById(createMatchRequest.profileId)
                .orElseThrow(() -> {
                    log.warn("Profile with id {} not found", createMatchRequest.profileId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Profile with id " + createMatchRequest.profileId + " not found");
                });

        // TODO: Make sure there are no existing conversations with this profile already

        // Continue creating conversation
        Conversation conversation = new Conversation(UUID.randomUUID().toString(),
                createMatchRequest.profileId, Collections.emptyList());
        Conversation createdConversation = conversationRepository.save(conversation);
        log.info("Conversation created: {}", createdConversation);

        // Continue creating match
        Match match = new Match(UUID.randomUUID().toString(), profile, createdConversation.conversationId());
        Match createdMatch = matchRepository.save(match);
        log.info("Match created: {}", createdMatch);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public record CreateMatchRequest(String profileId) { }
}
