package io.github.stackpan.examia.server.http.resource;

import io.github.stackpan.examia.server.entity.Case;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Value
@Relation(collectionRelation = "cases", itemRelation = "case")
public class CaseResource extends RepresentationModel<CaseResource> {

    UUID id;

    String title;

    String description;

    Integer durationInSeconds;

    CaseOwnerResource user;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;

    public static CaseResource fromEntity(Case entity) {
        return new CaseResource(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDurationInSeconds(),
                CaseOwnerResource.fromEntity(entity.getUser()),
                entity.getCreatedAt().atOffset(ZoneOffset.UTC),
                entity.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }

}
