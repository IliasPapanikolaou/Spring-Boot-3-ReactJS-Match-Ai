package com.ipap.springboot3reactjsmatchai.profiles;

import org.springframework.data.annotation.Id;

public record Profile(
        @Id
        String id,
        String firstName,
        String lastName,
        Integer age,
        String ethnicity,
        Gender gender,
        String bio,
        String imageUrl,
        String myersBriggsPersonalityType
) {
}
