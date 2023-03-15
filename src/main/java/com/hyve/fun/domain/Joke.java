package com.hyve.fun.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "joke")
@Entity
@Getter
@Setter
public class Joke implements Serializable {

    @Id
    private String id;

//    @JsonProperty("icon_url")
//    private String iconUrl;
//    private String url;
    @JsonProperty("value")
    private String message;

//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    @JsonProperty("created_at")
//    private LocalDateTime createdAt;
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonProperty("updated_at")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime updatedAt;
//    private String[] categories;

    @OneToMany(mappedBy = "joke")
    @JsonIgnore
    private List<UserJoke> userJoke;
}
