package com.poll.pollapp.service;

import com.poll.pollapp.domain.User;
import com.poll.pollapp.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo users;

    public UserService(UserRepo users) {
        this.users = users;
    }

    public List<User> findAll() {
        return users.findAll();
    }

    public User get(UUID id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User create(String username, String email) {
        return users.save(User.builder().username(username).email(email).build());
    }

    public User update(UUID id, String username, String email) {
        User u = get(id);
        if (username != null) u.setUsername(username);
        if (email != null) u.setEmail(email);
        return users.save(u);
    }

    public void delete(UUID id) {
        users.deleteById(id);
    }
}
