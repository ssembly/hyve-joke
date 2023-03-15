package com.hyve.fun.web.rest;


import com.hyve.fun.domain.Joke;
import com.hyve.fun.domain.JokeStat;
import com.hyve.fun.repository.UserRepository;
import com.hyve.fun.service.BaseService;
import com.hyve.fun.service.JokeService;
import com.hyve.fun.errors.NoJokeException;
import com.hyve.fun.errors.OutOfJokeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JokeController extends BaseService {

    private final JokeService jokeService;

    public JokeController(JokeService jokeService, UserRepository userRepository) {
        super(userRepository);
        this.jokeService = jokeService;
    }

    @GetMapping("/jokes/retrieve")
    public ResponseEntity<List<Joke>> retrieveAndSaveJokes() throws NoJokeException {
        return new ResponseEntity<>(jokeService.retrieveAndSaveJokes(), HttpStatus.FOUND);
    }

    @GetMapping("/jokes/random/{count}")
    public ResponseEntity<List<Joke>> getRandomJokes(
            @PathVariable int count,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String user)
            throws OutOfJokeException {
        return new ResponseEntity<>(jokeService.getRandomJokes(getUserId(user), count), HttpStatus.FOUND);
    }

    @GetMapping("/jokes/stats")
    public ResponseEntity<List<JokeStat>> getJokeStats() {
        return new ResponseEntity<>(jokeService.getJokeStats(), HttpStatus.FOUND);
    }
}
