package io.github.stackpan.examia.server.examiaserver.http.resource;

import io.github.stackpan.examia.server.examiaserver.entity.Case;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.OffsetDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Value
public class CaseResource extends RepresentationModel<CaseResource> {

    UUID id;

    String title;

    String description;

    Integer durationInSeconds;

    CaseOwnerResource owner;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;

    public static CaseResource fromEntity(Case entity) {
        return new CaseResource(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDurationInSeconds(),
                CaseOwnerResource.fromEntity(entity.getOwner()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
