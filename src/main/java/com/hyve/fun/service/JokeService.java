package com.hyve.fun.service;


import com.hyve.fun.domain.*;
import com.hyve.fun.repository.JokeRepository;
import com.hyve.fun.repository.UserJokeRepository;
import com.hyve.fun.repository.UserRepository;
import com.hyve.fun.errors.NoJokeException;
import com.hyve.fun.errors.OutOfJokeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JokeService {

    private final Logger log = LoggerFactory.getLogger(JokeService.class);

    @Value("${joke.api.url}")
    private String url;

    @Value("${joke.time-window}")
    private int timeWindow;

    private final UserRepository userRepository;
    private final JokeRepository jokeRepository;
    private final UserJokeRepository userJokeRepository;

    @Transactional
    public List<Joke> retrieveAndSaveJokes() throws NoJokeException {
        final Joke[] retrievedJokes = retrieveJokes();
        jokeRepository.deleteAll();
        jokeRepository.flush();
        return jokeRepository.saveAll(Arrays.asList(retrievedJokes));
    }

    public Joke[] retrieveJokes() throws NoJokeException {
        final ResponseEntity<JokeResponse> response
                = new RestTemplate().getForEntity(url, JokeResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            final JokeResponse jokeResponse = response.getBody();
            assert jokeResponse != null;
            return jokeResponse.getResult();
        } else throw new NoJokeException();
    }

    @Transactional
    public List<Joke> getRandomJokes(Long userId, int count) throws OutOfJokeException {
        log.debug("Retrieve {} random jokes for user {}", count, userId);
        final Pageable limit = PageRequest.of(0, count);
        final List<Joke> randomJokes = jokeRepository
                .findAllByUserJokeIsNullOrUserJokeUserIdAndUserJokeActive(userId, false, limit);
        if (randomJokes.isEmpty()) {
            throw new OutOfJokeException();
        }
        randomJokes.forEach(joke ->
                userJokeRepository.save(new UserJoke(null, userId, joke, true, Instant.now())));
        return randomJokes;
    }

    public List<JokeStat> getJokeStats() {
        final List<UserJoke> userJokes = userJokeRepository.findAll();
        final List<JokeStat> jokeStats = new ArrayList<>();
        userJokes.forEach(userJoke -> {
            createOrUpdateJokeStat(jokeStats, userJoke);
        });
        return jokeStats;
    }

    private void createOrUpdateJokeStat(List<JokeStat> jokeStats, UserJoke userJoke) {
        if (jokeStats.isEmpty() || jokeStats.stream().noneMatch(jokeStat -> jokeStat.getJoke().getId().equals(userJoke.getJoke().getId()))) {
            createNewJokeStat(jokeStats, userJoke);
        } else {
            jokeStats.forEach(jokeStat -> {
                updateExistingJokeStat(userJoke, jokeStat);
            });
        }
    }

    private void updateExistingJokeStat(UserJoke userJoke, JokeStat jokeStat) {
        if (jokeStat.getJoke().getId().equals(userJoke.getJoke().getId())) {
            jokeStat.setJokeCount(jokeStat.getJokeCount() + 1);
            try {
                jokeStat.setUserStats(mapToUserStat(userJoke.getUserId(), jokeStat.getUserStats()));
            } catch (OutOfJokeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createNewJokeStat(List<JokeStat> jokeStats, UserJoke userJoke) {
        try {
            jokeStats.add(new JokeStat(userJoke.getJoke(), 1, mapToUserStat(userJoke.getUserId(), null)));
        } catch (OutOfJokeException e) {
            throw new RuntimeException(e);
        }
    }

    private List<UserStat> mapToUserStat(Long userId, List<UserStat> existing) throws OutOfJokeException {
        if (existing == null || existing.stream().noneMatch(userStat -> userStat.getUser().getId().equals(userId))) {
            return createNewUserStat(userId);
        } else {
            updateExistingUserStat(userId, existing);
            return existing;
        }
    }

    private static void updateExistingUserStat(Long userId, List<UserStat> existing) {
        existing.forEach(userStat -> {
            if (userStat.getUser().getId().equals(userId)) {
                userStat.setJokeCount(userStat.getJokeCount() + 1);
            }
        });
    }

    private List<UserStat> createNewUserStat(Long userId) {
        final Optional<User> user = userRepository.findById(userId);
        return user.map(value -> Collections.singletonList(new UserStat(value, 1))).orElseThrow(OutOfJokeException::new);
    }

    @Scheduled(fixedDelay = 60000)
    public void disableJokes() {
        log.debug("Running scheduled task to disable expired jokes per user");
        userJokeRepository
                .findAllByActiveAndCreatedAtAfter(true, Instant.now().plusSeconds(timeWindow * 1000L))
                .ifPresent(userJokes -> userJokes.forEach(userJoke -> {
                    log.debug("Disabling joke {} for user {}", userJoke.getJoke().getId(), userJoke.getUserId());
                    userJoke.setActive(false);
                    userJokeRepository.save(userJoke);
                }));
    }
}
