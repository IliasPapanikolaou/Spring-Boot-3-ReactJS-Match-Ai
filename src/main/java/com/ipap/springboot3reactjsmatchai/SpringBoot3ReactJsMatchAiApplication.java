package com.ipap.springboot3reactjsmatchai;

import com.ipap.springboot3reactjsmatchai.conversations.ChatMessage;
import com.ipap.springboot3reactjsmatchai.conversations.Conversation;
import com.ipap.springboot3reactjsmatchai.conversations.ConversationRepository;
import com.ipap.springboot3reactjsmatchai.profiles.Gender;
import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import com.ipap.springboot3reactjsmatchai.profiles.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3ReactJsMatchAiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Running Match AI Application");

        Profile profile = new Profile("1",
                "John", "Rambo", 40, "American", Gender.MALE, "Fighter",
                "foo.jpg", "INTP");

        //profileRepository.save(profile);
        profileRepository.findAll().forEach(System.out::println);

        Conversation conversation = new Conversation("1", profile.profileId(),
                List.of(new ChatMessage("1", "Hello World", LocalDateTime.now())));

        //conversationRepository.save(conversation);
        conversationRepository.findAll().forEach(System.out::println);
    }
}
