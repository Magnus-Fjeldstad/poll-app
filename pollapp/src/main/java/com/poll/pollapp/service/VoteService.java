package com.poll.pollapp.service;

import com.poll.pollapp.domain.Vote;
import com.poll.pollapp.manager.PollManager;
import com.poll.pollapp.repo.VoteRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VoteService {
    private final VoteRepo votes;
    private final PollManager manager;

    public VoteService(VoteRepo votes, PollManager manager) {
        this.votes = votes;
        this.manager = manager;
    }

    public Vote get(UUID id) {
        return votes.findById(id).orElseThrow(() -> new IllegalArgumentException("Vote not found"));
    }

    public List<Vote> listByPoll(UUID pollId) {
        return votes.findByPollId(pollId);
    }

    public Vote castOrChange(UUID pollId, UUID userId, UUID optionId) {
        return manager.castOrChangeVote(pollId, userId, optionId);
    }

    public void delete(UUID id) {
        votes.deleteById(id);
    }

    public Vote voteOption(UUID optionId, UUID userId, int value) {
        return manager.castOrChangeOptionVote(optionId, userId, value);
    }

    public int scoreForOption(UUID optionId) {
        return manager.scoreForOption(optionId);
    }

    public long countForOption(UUID optionId) {
        return manager.countForOption(optionId);
    }
}
