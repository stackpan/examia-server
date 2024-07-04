package io.github.stackpan.examia.server.assembler;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.http.controller.CaseController;
import io.github.stackpan.examia.server.http.resource.CaseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
public class CaseModelAssembler implements RepresentationModelAssembler<Case, EntityModel<CaseResource>> {

    private final JwtAuthenticationToken authentication;

    @Override
    public EntityModel<CaseResource> toModel(Case entity) {
        return EntityModel.of(
                CaseResource.fromEntity(entity),
                WebMvcLinkBuilder.linkTo(methodOn(CaseController.class).getCase(entity.getId().toString(), authentication)).withSelfRel(),
                linkTo(methodOn(CaseController.class).listCases(Pageable.unpaged(), authentication)).withRel("cases")
        );
    }
}
