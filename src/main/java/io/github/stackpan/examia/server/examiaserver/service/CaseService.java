package io.github.stackpan.examia.server.examiaserver.service;

import io.github.stackpan.examia.server.examiaserver.model.Case;
import io.github.stackpan.examia.server.examiaserver.model.NewCase;

import java.util.List;
import java.util.UUID;

public interface CaseService {

    List<Case> getAll();

    Case getById(UUID id);

    Case create(NewCase model);

    void updateById(UUID id, NewCase model);

    void deleteById(UUID id);

}
