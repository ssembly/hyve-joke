package com.hyve.fun.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JokeResponse {

    private int total;
    private Joke[] result;

}
