package com.poll.pollapp.repo;

import com.poll.pollapp.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo {
    List<User> findAll();

    Optional<User> findById(UUID id);

    User save(User u);

    void deleteById(UUID id);
}