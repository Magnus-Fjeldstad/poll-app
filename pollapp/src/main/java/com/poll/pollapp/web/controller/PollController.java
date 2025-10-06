package com.poll.pollapp.web.controller;

import com.poll.pollapp.domain.Poll;
import com.poll.pollapp.domain.VoteOption;
import com.poll.pollapp.service.PollService;
import com.poll.pollapp.web.dto.PollDtos.CreatePollRequest;
import com.poll.pollapp.web.dto.PollDtos.PollSummary;
import com.poll.pollapp.web.dto.PollDtos.UpdatePollRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/polls")
public class PollController {
    private final PollService service;

    public PollController(PollService service) {
        this.service = service;
    }

    @GetMapping
    public List<PollSummary> list() {
        return service.findAll().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Poll get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody CreatePollRequest req) {
        List<VoteOption> options = req.getOptions() == null ? List.of() :
                req.getOptions().stream()
                        .map(o -> VoteOption.builder()
                                .caption(o.getCaption())
                                .presentationOrder(o.getPresentationOrder())
                                .build())
                        .collect(Collectors.toList());

        Poll created = service.create(
                req.getCreatorId(),
                req.getQuestion(),
                req.getValidUntil(),
                options
        );

        return ResponseEntity
                .created(URI.create("/api/polls/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public Poll update(@PathVariable UUID id, @RequestBody UpdatePollRequest req) {
        Instant validUntil = req.getValidUntil();
        return service.update(id, req.getQuestion(), validUntil);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<Map<Integer, Long>> getAggregatedVotes(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getAggregatedVotes(id));
    }

    @GetMapping("/{id}/options")
    public List<VoteOption> listOptions(@PathVariable UUID id) {
        return service.listOptions(id);
    }

    private PollSummary toSummary(Poll p) {
        PollSummary s = new PollSummary();
        s.setId(p.getId());
        s.setQuestion(p.getQuestion());
        s.setPublishedAt(p.getPublishedAt());
        s.setValidUntil(p.getValidUntil());
        s.setCreatedBy(p.getCreatedBy() != null ? p.getCreatedBy().getId() : null);
        s.setOptionCount(p.getOptions().size());
        s.setVoteCount(p.getVotes().size());
        return s;
    }
}
