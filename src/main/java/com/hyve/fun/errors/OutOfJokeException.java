package com.hyve.fun.errors;

public class OutOfJokeException extends BadRequestAlertException{

    public OutOfJokeException() {
        super("Out of jokes, now that's funny!", "Joke", "outOfJokes");
    }
}
