package com.poll.pollapp.service;

import com.poll.pollapp.cache.PollVoteCache;
import com.poll.pollapp.domain.Poll;
import com.poll.pollapp.domain.User;
import com.poll.pollapp.domain.VoteOption;
import com.poll.pollapp.manager.PollManager;
import com.poll.pollapp.repo.PollRepo;
import com.poll.pollapp.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PollService {
    private final PollRepo polls;
    private final UserRepo users;
    private final PollManager manager;
    private final PollVoteCache pollVoteCache;

    public PollService(PollRepo polls, UserRepo users, PollManager manager, PollVoteCache pollVoteCache) {
        this.polls = polls;
        this.users = users;
        this.manager = manager;
        this.pollVoteCache = pollVoteCache;
    }

    public Map<Integer, Long> getAggregatedVotes(UUID pollId) {
        // 1. Sjekk cache
        Map<String, String> cached = pollVoteCache.getVotes(pollId);
        if (cached != null && !cached.isEmpty()) {
            return cached.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> Integer.parseInt(e.getKey()),
                            e -> Long.parseLong(e.getValue())
                    ));
        }

        Poll poll = get(pollId);
        Map<Integer, Long> counts = poll.getOptions().stream()
                .collect(Collectors.toMap(
                        VoteOption::getPresentationOrder,
                        opt -> poll.getVotes().stream()
                                .filter(v -> v.getOption() != null && v.getOption().getId().equals(opt.getId()))
                                .count()
                ));
        pollVoteCache.putVotes(pollId, counts);
        System.out.println("Aggregated votes for poll " + pollId + ": " + counts);

        return counts;
    }

    public List<Poll> findAll() {
        return polls.findAll();
    }

    public Poll get(UUID id) {
        return polls.findById(id).orElseThrow(() -> new IllegalArgumentException("Poll not found"));
    }

    public Poll create(UUID creatorId, String question, Instant validUntil, List<VoteOption> options) {
        User creator = users.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));

        Poll created = manager.createPoll(creator, question, Instant.now(), validUntil, options);

        return polls.save(created);
    }

    public Poll update(UUID id, String question, Instant validUntil) {
        Poll p = get(id);
        if (question != null) p.setQuestion(question);
        p.setValidUntil(validUntil);
        return polls.save(p);
    }

    public void delete(UUID id) {
        polls.deleteById(id);
    }

    public List<VoteOption> listOptions(UUID pollId) {
        return new ArrayList<>(get(pollId).getOptions());
    }
}
