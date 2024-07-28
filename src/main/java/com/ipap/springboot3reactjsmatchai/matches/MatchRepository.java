package com.ipap.springboot3reactjsmatchai.matches;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
}
