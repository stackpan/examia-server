package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.assembler.CaseModelAssembler;
import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.http.request.UpdateCaseRequest;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    private final PagedResourcesAssembler<Case> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<CaseResource>> listCases(@PageableDefault(sort = "title") Pageable pageable, JwtAuthenticationToken jwt) {
        var casePage = caseService.getAllByUserId(pageable, (String) jwt.getTokenAttributes().get("sub"));

        return pagedResourcesAssembler.toModel(casePage, new CaseModelAssembler(jwt));
    }

    @PostMapping
    public ResponseEntity<EntityModel<CaseResource>> createCase(@RequestBody @Valid CreateCaseRequest request, JwtAuthenticationToken jwt) {
        var created = caseService.createByUserId(request, (String) jwt.getTokenAttributes().get("sub"));

        var caseModelAssembler = new CaseModelAssembler(jwt);
        var entityModel = caseModelAssembler.toModel(created);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{caseId}")
    public EntityModel<CaseResource> getCase(@PathVariable String caseId, JwtAuthenticationToken jwt) {
        var aCase = caseService.getByCaseIdAndUserId(caseId, (String) jwt.getTokenAttributes().get("sub"));

        var caseModelAssembler = new CaseModelAssembler(jwt);
        return caseModelAssembler.toModel(aCase);
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<Void> updateCase(@PathVariable String caseId, @RequestBody @Valid UpdateCaseRequest request, JwtAuthenticationToken jwt) {
        caseService.updateByCaseIdAndUserId(caseId, (String) jwt.getTokenAttributes().get("sub"), request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Void> deleteCase(@PathVariable String caseId, JwtAuthenticationToken jwt) {
        caseService.deleteByCaseIdAndUserId(caseId, (String) jwt.getTokenAttributes().get("sub"));

        return ResponseEntity.noContent().build();
    }

}
