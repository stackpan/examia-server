package io.github.stackpan.examia.server.http.resource;

import io.github.stackpan.examia.server.entity.User;

import java.util.UUID;

public record CaseOwnerResource(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role
) {

    public static CaseOwnerResource fromEntity(User entity) {
        return new CaseOwnerResource(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );
    }

}
