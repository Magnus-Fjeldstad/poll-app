package com.poll.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
/**
 * User class
 * Uses Lombok annotations for getters, setters, equals, hashCode, toString, and builder
 */
public class User {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String username;
    private String email;

    @Builder.Default
    private List<Poll> created = new ArrayList<>();

    @Builder.Default
    private List<Vote> voted = new ArrayList<>();
}