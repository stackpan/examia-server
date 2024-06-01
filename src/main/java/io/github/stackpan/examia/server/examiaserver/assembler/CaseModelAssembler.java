package io.github.stackpan.examia.server.examiaserver.assembler;

import io.github.stackpan.examia.server.examiaserver.entity.Case;
import io.github.stackpan.examia.server.examiaserver.http.controller.CaseController;
import io.github.stackpan.examia.server.examiaserver.http.resource.CaseResource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CaseModelAssembler implements RepresentationModelAssembler<Case, EntityModel<CaseResource>> {

    @Override
    public EntityModel<CaseResource> toModel(Case entity) {
        return EntityModel.of(
                CaseResource.fromEntity(entity),
                linkTo(methodOn(CaseController.class).getCase(entity.getId().toString())).withSelfRel(),
                linkTo(methodOn(CaseController.class).listCases(Pageable.unpaged())).withRel("cases")
        );
    }
}
