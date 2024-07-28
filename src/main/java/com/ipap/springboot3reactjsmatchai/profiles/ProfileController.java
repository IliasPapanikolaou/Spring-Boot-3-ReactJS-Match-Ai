package com.ipap.springboot3reactjsmatchai.profiles;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileRepository profileRepository;

    @GetMapping("/random")
    public ResponseEntity<Profile> getRandomProfile() {
        return new ResponseEntity<>(profileRepository.getRandomProfile(), HttpStatus.OK);
    }

}
