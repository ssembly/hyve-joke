package com.hyve.fun.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JokeStat {

    private Joke joke;
    private int JokeCount;
    private List<UserStat> userStats;
}
