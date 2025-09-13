package com.poll.pollapp.web.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PollDtos {
    @Data
    public static class CreatePollRequest {
        private UUID creatorId;
        private String question;
        private Instant validUntil;
        private List<CreateOption> options;

        @Data
        public static class CreateOption {
            private String caption;
            private int presentationOrder;
        }
    }

    @Data
    public static class UpdatePollRequest {
        private String question;
        private Instant validUntil;
    }

    @Data
    public static class PollSummary {
        private UUID id;
        private String question;
        private Instant publishedAt;
        private Instant validUntil;
        private UUID createdBy;
        private int optionCount;
        private int voteCount;
    }
}
