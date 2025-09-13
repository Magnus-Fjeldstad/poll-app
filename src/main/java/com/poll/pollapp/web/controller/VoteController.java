package com.poll.pollapp.web.controller;

import com.poll.pollapp.domain.Vote;
import com.poll.pollapp.service.VoteService;
import com.poll.pollapp.web.dto.VoteDtos.CastVoteRequest;
import com.poll.pollapp.web.dto.VoteDtos.VoteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/polls/{pollId}/votes")
public class VoteController {
    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<VoteResponse> list(@PathVariable UUID pollId) {
        return service.listByPoll(pollId).stream().map(this::toResp).collect(Collectors.toList());
    }

    // Cast or change vote (idempotent for a user+poll)
    @PutMapping
    public ResponseEntity<VoteResponse> castOrChange(
            @PathVariable UUID pollId,
            @RequestBody CastVoteRequest req) {
        Vote v = service.castOrChange(pollId, req.getUserId(), req.getOptionId());
        VoteResponse resp = toResp(v);
        return ResponseEntity.created(URI.create("/api/polls/" + pollId + "/votes/" + v.getId())).body(resp);
    }

    @GetMapping("/{voteId}")
    public VoteResponse get(@PathVariable UUID pollId, @PathVariable UUID voteId) {
        return toResp(service.get(voteId));
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> delete(@PathVariable UUID pollId, @PathVariable UUID voteId) {
        service.delete(voteId);
        return ResponseEntity.noContent().build();
    }

    private VoteResponse toResp(Vote v) {
        VoteResponse r = new VoteResponse();
        r.setId(v.getId());
        r.setPublishedAt(v.getPublishedAt());
        r.setVoterId(v.getVoter() != null ? v.getVoter().getId() : null);
        r.setOptionId(v.getOption() != null ? v.getOption().getId() : null);
        r.setPollId(v.getOption() != null && v.getOption().getPoll() != null ? v.getOption().getPoll().getId() : null);
        return r;
    }
}
