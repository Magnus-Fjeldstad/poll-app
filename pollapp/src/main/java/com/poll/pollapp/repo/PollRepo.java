package com.poll.pollapp.repo;

import com.poll.pollapp.domain.Poll;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PollRepo {
    List<Poll> findAll();

    Optional<Poll> findById(UUID id);

    Poll save(Poll p);
    
    void deleteById(UUID id);
}