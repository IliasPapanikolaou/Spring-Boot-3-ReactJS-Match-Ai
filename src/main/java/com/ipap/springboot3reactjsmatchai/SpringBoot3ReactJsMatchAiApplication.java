package com.ipap.springboot3reactjsmatchai;

import com.ipap.springboot3reactjsmatchai.conversations.ChatMessage;
import com.ipap.springboot3reactjsmatchai.conversations.Conversation;
import com.ipap.springboot3reactjsmatchai.conversations.ConversationRepository;
import com.ipap.springboot3reactjsmatchai.profiles.Gender;
import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class SpringBoot3ReactJsMatchAiApplication implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;
    private final OllamaChatModel chatModel;

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3ReactJsMatchAiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Running Match AI Application");

        // Open Ai ChatBot
        ChatResponse response = chatModel.call(
                new Prompt("Who is Ilias Papanikolaou"));

        System.out.println(response.getResult());

        // Delete all test data
        profileRepository.deleteAll();
        conversationRepository.deleteAll();

        Profile profile_male = new Profile("1",
                "John", "Rambo", 40, "American", Gender.MALE, "Fighter",
                "foo_male.jpg", "INTP");

        Profile profile_female = new Profile("2",
                "Maria", "Spendings", 30, "American", Gender.FEMALE, "Shopping",
                "foo_female.jpg", "INTP");

        profileRepository.save(profile_male);
        profileRepository.save(profile_female);
        profileRepository.findAll().forEach(p -> log.info(p.toString()));

        Conversation conversation = new Conversation("1", profile_male.profileId(),
                List.of(new ChatMessage("1", "Hello Maria!", LocalDateTime.now())));

        conversationRepository.save(conversation);
        conversationRepository.findAll().forEach(c -> log.info(c.toString()));
    }
}
