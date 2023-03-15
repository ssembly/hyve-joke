package com.hyve.fun.repository;


import com.hyve.fun.domain.Joke;
import com.hyve.fun.domain.UserJoke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserJokeRepository extends JpaRepository<UserJoke, String> {

    Optional<List<UserJoke>> findAllByActiveAndCreatedAtAfter(boolean active, Instant date);
}
