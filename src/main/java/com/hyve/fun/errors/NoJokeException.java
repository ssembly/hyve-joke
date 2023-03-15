package com.hyve.fun.errors;

public class NoJokeException extends BadRequestAlertException{

    public NoJokeException() {
        super("No jokes found, nothing funny there", "Joke", "noJokesFound");
    }
}
