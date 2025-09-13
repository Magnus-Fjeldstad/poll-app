// src/main/java/com/example/pollapp/domain/VoteOption.java
package com.poll.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VoteOption {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String caption;
    private int presentationOrder;

    // back reference to the Poll aggregate
    private Poll poll;
}
