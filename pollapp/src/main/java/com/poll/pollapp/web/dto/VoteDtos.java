package com.poll.pollapp.web.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

public class VoteDtos {
    @Data
    public static class CastVoteRequest {
        private UUID userId;
        private UUID optionId;
    }

    @Data
    public static class VoteResponse {
        private UUID id;
        private Instant publishedAt;
        private UUID voterId;
        private UUID optionId;
        private UUID pollId;
    }
}
