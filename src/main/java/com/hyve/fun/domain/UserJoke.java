package com.hyve.fun.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Table(name = "user_joke")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoke implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private Joke joke;

    private boolean active;
    private Instant createdAt;
}
