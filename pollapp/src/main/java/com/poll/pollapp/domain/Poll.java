package com.poll.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
/**
 * Poll class
 * Uses Lombok annotations for getters, setters, equals, hashCode, toString, and builder
 */
public class Poll {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    private User createdBy;

    @Builder.Default
    private List<VoteOption> options = new ArrayList<>();

    @Builder.Default
    private List<Vote> votes = new ArrayList<>();
}
