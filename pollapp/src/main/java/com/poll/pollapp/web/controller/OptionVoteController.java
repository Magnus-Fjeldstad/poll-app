package com.poll.pollapp.web.controller;

import com.poll.pollapp.service.VoteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/options/{optionId}")
public class OptionVoteController {
    private final VoteService service;

    public OptionVoteController(VoteService service) {
        this.service = service;
    }

    @Setter
    @Getter
    public static class OptionVoteRequest {
        private UUID userId;
        private int value;
    }

    @Getter
    @Setter
    public static class OptionVoteSummary {
        public UUID optionId;
        public int score;
        public long votes;
    }

    @PutMapping("/vote")
    public OptionVoteSummary vote(@PathVariable UUID optionId, @RequestBody OptionVoteRequest req) {
        service.voteOption(optionId, req.getUserId(), req.getValue());
        OptionVoteSummary s = new OptionVoteSummary();
        s.optionId = optionId;
        s.score = service.scoreForOption(optionId);
        s.votes = service.countForOption(optionId);
        return s;
    }

    @GetMapping("/stats")
    public OptionVoteSummary stats(@PathVariable UUID optionId) {
        OptionVoteSummary s = new OptionVoteSummary();
        s.optionId = optionId;
        s.score = service.scoreForOption(optionId);
        s.votes = service.countForOption(optionId);
        return s;
    }
}
