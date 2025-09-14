package com.poll.pollapp.web.dto;

import lombok.Data;

import java.util.UUID;

public class UserDtos {
    @Data
    public static class CreateUserRequest {
        private String username;
        private String email;
    }

    @Data
    public static class UpdateUserRequest {
        private String username;
        private String email;
    }

    @Data
    public static class UserResponse {
        private UUID id;
        private String username;
        private String email;
    }
}