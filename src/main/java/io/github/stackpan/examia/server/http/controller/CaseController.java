package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.assembler.CaseModelAssembler;
import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.http.resource.CaseResource;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.service.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    private final CaseModelAssembler caseModelAssembler;

    private final PagedResourcesAssembler<Case> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<CaseResource>> listCases(@PageableDefault(sort = "title") Pageable pageable) {
        var casePage = caseService.getAll(pageable);

        return pagedResourcesAssembler.toModel(casePage, caseModelAssembler);
    }

    @PostMapping
    public ResponseEntity<EntityModel<CaseResource>> createCase(@RequestBody @Valid CreateCaseRequest request) {
        var created = caseService.create(request);

        var entityModel = caseModelAssembler.toModel(created);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{caseId}")
    public EntityModel<CaseResource> getCase(@PathVariable String caseId) {
        var aCase = caseService.getById(UUID.fromString(caseId));

        return caseModelAssembler.toModel(aCase);
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<Void> updateCase(@PathVariable String caseId, @RequestBody @Valid CreateCaseRequest request) {
        caseService.updateById(UUID.fromString(caseId), request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Void> deleteCase(@PathVariable String caseId) {
        caseService.deleteById(UUID.fromString(caseId));

        return ResponseEntity.noContent().build();
    }

}
