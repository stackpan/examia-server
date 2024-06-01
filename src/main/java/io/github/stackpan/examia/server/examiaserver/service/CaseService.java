package io.github.stackpan.examia.server.examiaserver.service;

import io.github.stackpan.examia.server.examiaserver.entity.Case;
import io.github.stackpan.examia.server.examiaserver.http.request.CreateCaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CaseService {

    Page<Case> getAll(Pageable pageable);

    Case getById(UUID id);

    Case create(CreateCaseRequest model);

    void updateById(UUID id, CreateCaseRequest model);

    void deleteById(UUID id);

}
