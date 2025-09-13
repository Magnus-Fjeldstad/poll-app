package com.poll.pollapp.repo;

import com.poll.pollapp.domain.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepo {
    Optional<Vote> findById(UUID id);

    List<Vote> findByPollId(UUID pollId);

    void deleteById(UUID id);
}