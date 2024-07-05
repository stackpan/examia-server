package io.github.stackpan.examia.server.repository;

import io.github.stackpan.examia.server.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface IssueRepository extends CrudRepository<Issue, UUID>, PagingAndSortingRepository<Issue, UUID> {

    Page<Issue> findAllByExamiaCaseId(UUID caseId, Pageable pageable);

}
