package io.github.stackpan.examia.server.examiaserver.repository;

import io.github.stackpan.examia.server.examiaserver.entity.CaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CaseRepository extends CrudRepository<CaseEntity, UUID> {
}
