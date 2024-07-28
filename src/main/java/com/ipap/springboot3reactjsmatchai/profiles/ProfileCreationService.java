package com.ipap.springboot3reactjsmatchai.profiles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileCreationService {

    private static final String PROFILES_FILE_PATH = "profiles.json";

    private final ProfileRepository profileRepository;

    @Value("${startup-actions.initializeProfiles}")
    private Boolean initializeProfiles;

    @Value("${matchAi.lookingForGender}")
    private String lookingForGender;

    @Value("#{${matchAi.character.user}}")
    private Map<String, String> userProfileProperties;

    // Save all profiles from profiles.json file to DB
    public void saveProfilesToDB() {
        Gson gson = new Gson();
        try {
            List<Profile> existingProfiles = gson.fromJson(new FileReader(PROFILES_FILE_PATH),
                            new TypeToken<List<Profile>>() {}.getType());

            profileRepository.deleteAll();
            List<Profile> savedProfiles = profileRepository.saveAll(existingProfiles);
            savedProfiles.forEach(profile -> log.info("Profile saved to DB: {}", profile));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Create a user Profile
        Profile profile = new Profile(
                userProfileProperties.get("id"),
                userProfileProperties.get("firstName"),
                userProfileProperties.get("lastName"),
                Integer.parseInt(userProfileProperties.get("age")),
                userProfileProperties.get("ethnicity"),
                Gender.valueOf(userProfileProperties.get("gender")),
                userProfileProperties.get("bio"),
                userProfileProperties.get("imageUrl"),
                userProfileProperties.get("myersBriggsPersonalityType")
        );

        log.info("User profile saved to DB: {}", profile);
        profileRepository.save(profile);
    }





}
