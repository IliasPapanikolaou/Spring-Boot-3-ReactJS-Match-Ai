package com.ipap.springboot3reactjsmatchai;

import com.ipap.springboot3reactjsmatchai.conversations.ConversationRepository;
import com.ipap.springboot3reactjsmatchai.matches.MatchRepository;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileCreationService;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class SpringBoot3ReactJsMatchAiApplication implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;
    private final MatchRepository matchRepository;
    private final ProfileCreationService profileCreationService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3ReactJsMatchAiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Running Match AI Application");

        // Clear all data from DB
        clearAllData();

        // Save all female data from profiles.json to DB and create a default male user profile
        profileCreationService.saveProfilesToDB();
        log.info("Profiles count saved to DB: {}", profileRepository.count());
    }

    private void clearAllData() {
        profileRepository.deleteAll();
        conversationRepository.deleteAll();
        matchRepository.deleteAll();
    }
}
