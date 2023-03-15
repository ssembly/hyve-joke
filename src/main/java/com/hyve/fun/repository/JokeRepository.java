package com.hyve.fun.repository;


import com.hyve.fun.domain.Joke;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JokeRepository extends JpaRepository<Joke, String> {

    List<Joke> findAllByUserJokeIsNullOrUserJokeUserIdAndUserJokeActive(Long userId, boolean active, Pageable page);
}
