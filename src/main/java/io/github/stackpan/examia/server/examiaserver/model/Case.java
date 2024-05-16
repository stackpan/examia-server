package io.github.stackpan.examia.server.examiaserver.model;

import io.github.stackpan.examia.server.examiaserver.entity.CaseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Case(
        UUID id,
        String title,
        String description,
        Integer durationInSeconds,
        CaseOwner owner,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static Case fromEntity(CaseEntity entity) {
        return new Case(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDurationInSeconds(),
                CaseOwner.fromEntity(entity.getOwner()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
