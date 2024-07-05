package io.github.stackpan.examia.server.http.resource;

import io.github.stackpan.examia.server.data.enums.IssueType;
import io.github.stackpan.examia.server.entity.Issue;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Value
@Relation(collectionRelation = "issues", itemRelation = "issue")
public class IssueResource extends RepresentationModel<IssueResource> {

    UUID id;

    Long sequence;

    IssueType type;

    String body;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;

    public static IssueResource fromEntity(Issue entity) {
        return new IssueResource(
                entity.getId(),
                entity.getSequence(),
                entity.getType(),
                entity.getBody(),
                entity.getCreatedAt().atOffset(ZoneOffset.UTC),
                entity.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }

}
