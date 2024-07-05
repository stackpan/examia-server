package io.github.stackpan.examia.server.assembler;

import io.github.stackpan.examia.server.entity.Issue;
import io.github.stackpan.examia.server.http.controller.IssueController;
import io.github.stackpan.examia.server.http.resource.IssueResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
public class IssueModelAssembler implements RepresentationModelAssembler<Issue, EntityModel<IssueResource>> {

    private final JwtAuthenticationToken authentication;

    @Override
    public EntityModel<IssueResource> toModel(Issue entity) {
        return EntityModel.of(
                IssueResource.fromEntity(entity),
                linkTo(methodOn(IssueController.class).getIssue(entity.getExamiaCase().getId().toString(), entity.getId().toString(), authentication)).withSelfRel(),
                linkTo(methodOn(IssueController.class).listIssues(entity.getExamiaCase().getId().toString(), Pageable.unpaged(), authentication)).withRel("issues")
        );
    }
}
