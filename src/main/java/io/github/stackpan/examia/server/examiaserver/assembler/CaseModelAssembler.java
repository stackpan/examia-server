package io.github.stackpan.examia.server.examiaserver.assembler;

import io.github.stackpan.examia.server.examiaserver.http.controller.CaseController;
import io.github.stackpan.examia.server.examiaserver.http.resource.CaseResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CaseModelAssembler implements RepresentationModelAssembler<CaseResource, EntityModel<CaseResource>> {

    @Override
    public EntityModel<CaseResource> toModel(CaseResource entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(CaseController.class).getCase(entity.id().toString())).withSelfRel(),
                linkTo(methodOn(CaseController.class).listCases()).withRel("cases")
        );
    }
}
