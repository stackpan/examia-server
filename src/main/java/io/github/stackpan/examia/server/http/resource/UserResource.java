package io.github.stackpan.examia.server.http.resource;

import io.github.stackpan.examia.server.entity.User;

import java.util.UUID;

public record UserResource(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role
) {

    public static UserResource fromEntity(User entity) {
        return new UserResource(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );
    }

}