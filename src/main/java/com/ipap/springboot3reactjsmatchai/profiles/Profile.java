package com.ipap.springboot3reactjsmatchai.profiles;

public record Profile(
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
