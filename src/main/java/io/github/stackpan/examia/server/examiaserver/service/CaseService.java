package io.github.stackpan.examia.server.examiaserver.service;

import io.github.stackpan.examia.server.examiaserver.http.resource.CaseResource;
import io.github.stackpan.examia.server.examiaserver.http.request.CreateCaseRequest;

import java.util.List;
import java.util.UUID;

public interface CaseService {

    List<CaseResource> getAll();

    CaseResource getById(UUID id);

    CaseResource create(CreateCaseRequest model);

    void updateById(UUID id, CreateCaseRequest model);

    void deleteById(UUID id);

}
