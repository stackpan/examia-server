package io.github.stackpan.examia.server.examiaserver.model;

import io.github.stackpan.examia.server.examiaserver.entity.UserEntity;

import java.util.UUID;

public record CaseOwner(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName
) {

    public static CaseOwner fromEntity(UserEntity entity) {
        return new CaseOwner(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName()
        );
    }

}
