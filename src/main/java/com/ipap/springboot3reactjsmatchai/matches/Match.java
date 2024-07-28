package com.ipap.springboot3reactjsmatchai.matches;

import com.ipap.springboot3reactjsmatchai.profiles.Profile;
import org.springframework.data.annotation.Id;

public record Match(
        @Id
        String matchId,
        Profile profile,
        String conversationId
) {
}
