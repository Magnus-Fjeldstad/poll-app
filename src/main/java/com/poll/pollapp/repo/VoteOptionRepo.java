package com.poll.pollapp.repo;

import com.poll.pollapp.domain.VoteOption;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteOptionRepo {
    Optional<VoteOption> findById(UUID id);

    List<VoteOption> findByPollId(UUID pollId);
}