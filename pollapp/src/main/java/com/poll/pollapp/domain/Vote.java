package com.poll.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
/**
 * Vote class
 * Uses Lombok annotations for getters, setters, equals, hashCode, toString, and builder
 */
public class Vote {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private Instant publishedAt;
    private User voter;
    private VoteOption option;
    private int value;
}
