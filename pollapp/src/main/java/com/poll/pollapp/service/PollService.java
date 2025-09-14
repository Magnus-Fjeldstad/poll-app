package com.poll.pollapp.service;

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
import java.util.UUID;

@Service
public class PollService {
    private final PollRepo polls;
    private final UserRepo users;
    private final PollManager manager;

    public PollService(PollRepo polls, UserRepo users, PollManager manager) {
        this.polls = polls;
        this.users = users;
        this.manager = manager;
        System.out.println("PollService.manager@" + System.identityHashCode(manager));
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
