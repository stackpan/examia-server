package io.github.stackpan.examia.server.examiaserver.controller;

import io.github.stackpan.examia.server.examiaserver.assembler.CaseModelAssembler;
import io.github.stackpan.examia.server.examiaserver.model.Case;
import io.github.stackpan.examia.server.examiaserver.model.NewCase;
import io.github.stackpan.examia.server.examiaserver.service.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    private final CaseModelAssembler caseModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Case>> listCases() {
        var cases = caseService.getAll().stream()
                .map(caseModelAssembler::toModel)
                .toList();

        return CollectionModel.of(cases, linkTo(methodOn(CaseController.class).listCases()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Case>> createCase(@RequestBody @Valid NewCase request) {
        var created = caseService.create(request);

        var entityModel = caseModelAssembler.toModel(created);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{caseId}")
    public EntityModel<Case> getCase(@PathVariable String caseId) {
        var aCase = caseService.getById(UUID.fromString(caseId));

        return caseModelAssembler.toModel(aCase);
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<Void> updateCase(@PathVariable String caseId, @RequestBody @Valid NewCase request) {
        caseService.updateById(UUID.fromString(caseId), request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Void> deleteCase(@PathVariable String caseId) {
        caseService.deleteById(UUID.fromString(caseId));

        return ResponseEntity.noContent().build();
    }

}
