package io.github.stackpan.examia.server.repository;

import io.github.stackpan.examia.server.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CaseRepository extends CrudRepository<Case, UUID>, PagingAndSortingRepository<Case, UUID> {

    Page<Case> findAllByUserId(UUID userId, Pageable pageable);

}
