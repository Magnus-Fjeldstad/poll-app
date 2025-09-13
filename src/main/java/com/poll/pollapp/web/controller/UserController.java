package com.poll.pollapp.web.controller;

import com.poll.pollapp.domain.User;
import com.poll.pollapp.service.UserService;
import com.poll.pollapp.web.dto.UserDtos.CreateUserRequest;
import com.poll.pollapp.web.dto.UserDtos.UpdateUserRequest;
import com.poll.pollapp.web.dto.UserDtos.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest req) {
        User u = service.create(req.getUsername(), req.getEmail());
        UserResponse resp = new UserResponse();
        resp.setId(u.getId());
        resp.setUsername(u.getUsername());
        resp.setEmail(u.getEmail());
        return ResponseEntity.created(URI.create("/api/users/" + u.getId())).body(resp);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody UpdateUserRequest req) {
        return service.update(id, req.getUsername(), req.getEmail());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
