package com.ipap.springboot3reactjsmatchai.profiles;

import org.springframework.data.annotation.Id;

public record Profile(
        @Id
        String profileId,
        String firstname,
        String lastname,
        Integer age,
        String ethnicity,
        Gender gender,
        String bio,
        String imageUrl,
        String myersBriggsPersonalityType
) {
}
