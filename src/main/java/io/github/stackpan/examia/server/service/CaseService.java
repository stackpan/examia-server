package io.github.stackpan.examia.server.service;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.http.request.UpdateCaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaseService {

    Page<Case> getAll(Pageable pageable);

    Case getById(String id);

    Case create(CreateCaseRequest model);

    void updateById(String id, UpdateCaseRequest model);

    void deleteById(String id);

}
