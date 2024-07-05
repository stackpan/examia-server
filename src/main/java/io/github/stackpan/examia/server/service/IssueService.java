package io.github.stackpan.examia.server.service;

import io.github.stackpan.examia.server.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {

    Page<Issue> getPaginatedByCaseId(String caseId, Pageable pageable, String userId);

}
