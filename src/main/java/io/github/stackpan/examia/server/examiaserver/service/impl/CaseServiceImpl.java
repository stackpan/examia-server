package io.github.stackpan.examia.server.examiaserver.service.impl;

import io.github.stackpan.examia.server.examiaserver.entity.Case;
import io.github.stackpan.examia.server.examiaserver.entity.User;
import io.github.stackpan.examia.server.examiaserver.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.examiaserver.repository.CaseRepository;
import io.github.stackpan.examia.server.examiaserver.repository.UserRepository;
import io.github.stackpan.examia.server.examiaserver.service.CaseService;
import io.github.stackpan.examia.server.examiaserver.http.resource.CaseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;

    private final UserRepository userRepository;

    @Override
    public List<CaseResource> getAll() {
        var caseEntities = (List<Case>) caseRepository.findAll();

        return caseEntities.stream()
                .map(CaseResource::fromEntity)
                .toList();
    }

    @Override
    public CaseResource getById(UUID id) {
        var caseEntity = findByIdOrThrow(id);

        return CaseResource.fromEntity(caseEntity);
    }

    @Override
    @Transactional
    public CaseResource create(CreateCaseRequest model) {
        var caseEntity = new Case();

        caseEntity.setTitle(model.title());
        caseEntity.setDescription(model.description());
        caseEntity.setDurationInSeconds(model.durationInSeconds());
        caseEntity.setOwner(createUser());

        caseRepository.save(caseEntity);

        return CaseResource.fromEntity(caseEntity);
    }

    @Override
    @Transactional
    public void updateById(UUID id, CreateCaseRequest model) {
        var caseEntity = findByIdOrThrow(id);

        caseEntity.setTitle(model.title());
        caseEntity.setDescription(model.description());
        caseEntity.setDurationInSeconds(model.durationInSeconds());

        caseRepository.save(caseEntity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!caseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        caseRepository.deleteById(id);
    }

    private Case findByIdOrThrow(UUID id) {
        return caseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private User createUser() {
        return userRepository.findByUsername("john_doe")
                .orElseGet(() -> {
                    var newUser = new User();
                    newUser.setUsername("john_doe");
                    newUser.setEmail("johndoe@example.com");
                    newUser.setFirstName("John");
                    newUser.setPassword("password");

                    userRepository.save(newUser);

                    return newUser;
                });
    }

}
