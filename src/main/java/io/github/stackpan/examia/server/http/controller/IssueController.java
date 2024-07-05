package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.assembler.IssueModelAssembler;
import io.github.stackpan.examia.server.entity.Issue;
import io.github.stackpan.examia.server.http.resource.IssueResource;
import io.github.stackpan.examia.server.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cases/{caseId}/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    private final PagedResourcesAssembler<Issue> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<IssueResource>> listIssues(@PathVariable String caseId, @PageableDefault Pageable pageable, JwtAuthenticationToken jwt) {
        var issuePage = issueService.getPaginatedByCaseId(caseId, pageable, (String) jwt.getTokenAttributes().get("sub"));

        return pagedResourcesAssembler.toModel(issuePage, new IssueModelAssembler(jwt));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<EntityModel<IssueResource>> getIssue(@PathVariable String caseId, @PathVariable String issueId, JwtAuthenticationToken jwt) {
        return null;
    }

}
