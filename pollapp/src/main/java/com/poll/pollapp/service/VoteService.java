package com.poll.pollapp.service;

import com.poll.pollapp.cache.PollVoteCache;
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
    private final PollVoteCache pollVoteCache;

    public VoteService(VoteRepo votes, PollManager manager, PollVoteCache pollVoteCache) {
        this.votes = votes;
        this.manager = manager;
        this.pollVoteCache = pollVoteCache;
    }

    public Vote get(UUID id) {
        return votes.findById(id).orElseThrow(() -> new IllegalArgumentException("Vote not found"));
    }

    public List<Vote> listByPoll(UUID pollId) {
        return votes.findByPollId(pollId);
    }

    public Vote castOrChange(UUID pollId, UUID userId, UUID optionId) {
        Vote v = manager.castOrChangeVote(pollId, userId, optionId);
        pollVoteCache.invalidate(pollId);
        return v;
    }

    public void delete(UUID id) {
        Vote v = get(id);
        votes.deleteById(id);
        pollVoteCache.invalidate(v.getOption().getPoll().getId());
    }

    public Vote voteOption(UUID optionId, UUID userId, int value) {
        Vote v = manager.castOrChangeOptionVote(optionId, userId, value);
        pollVoteCache.invalidate(v.getOption().getPoll().getId());
        return v;
    }

    public int scoreForOption(UUID optionId) {
        return manager.scoreForOption(optionId);
    }

    public long countForOption(UUID optionId) {
        return manager.countForOption(optionId);
    }
}
