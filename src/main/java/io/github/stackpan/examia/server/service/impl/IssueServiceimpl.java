package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.entity.Issue;
import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.repository.CaseRepository;
import io.github.stackpan.examia.server.repository.IssueRepository;
import io.github.stackpan.examia.server.service.IssueService;
import io.github.stackpan.examia.server.util.UUIDs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueServiceimpl implements IssueService {

    private final CaseRepository caseRepository;

    private final IssueRepository issueRepository;

    @Override
    public Page<Issue> getPaginatedByCaseId(String caseId, Pageable pageable, String userId) {
        caseRepository.findByIdAndUserId(
                UUIDs.fromString(caseId).orElseThrowsResourceNotFound(Case.class),
                UUIDs.fromString(userId).orElseThrowsResourceNotFound(User.class)
        ).orElseThrow(() -> new ResourceNotFoundException(Case.class.getSimpleName(), caseId));

        return issueRepository.findAllByExamiaCaseId(
                UUIDs.fromString(caseId).orElseThrowsResourceNotFound(Case.class),
                pageable
        );
    }
}
