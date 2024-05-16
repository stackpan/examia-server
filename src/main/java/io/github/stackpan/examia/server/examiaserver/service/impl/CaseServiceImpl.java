package io.github.stackpan.examia.server.examiaserver.service.impl;

import io.github.stackpan.examia.server.examiaserver.entity.CaseEntity;
import io.github.stackpan.examia.server.examiaserver.entity.UserEntity;
import io.github.stackpan.examia.server.examiaserver.model.NewCase;
import io.github.stackpan.examia.server.examiaserver.repository.CaseRepository;
import io.github.stackpan.examia.server.examiaserver.repository.UserRepository;
import io.github.stackpan.examia.server.examiaserver.service.CaseService;
import io.github.stackpan.examia.server.examiaserver.model.Case;
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
    public List<Case> getAll() {
        var caseEntities = (List<CaseEntity>) caseRepository.findAll();

        return caseEntities.stream()
                .map(Case::fromEntity)
                .toList();
    }

    @Override
    public Case getById(UUID id) {
        var caseEntity = findByIdOrThrow(id);

        return Case.fromEntity(caseEntity);
    }

    @Override
    @Transactional
    public Case create(NewCase model) {
        var caseEntity = new CaseEntity();

        caseEntity.setTitle(model.title());
        caseEntity.setDescription(model.description());
        caseEntity.setDurationInSeconds(model.durationInSeconds());
        caseEntity.setOwner(createUser());

        caseRepository.save(caseEntity);

        return Case.fromEntity(caseEntity);
    }

    @Override
    @Transactional
    public void updateById(UUID id, NewCase model) {
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

    private CaseEntity findByIdOrThrow(UUID id) {
        return caseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private UserEntity createUser() {
        return userRepository.findByUsername("john_doe")
                .orElseGet(() -> {
                    var newUser = new UserEntity();
                    newUser.setUsername("john_doe");
                    newUser.setEmail("johndoe@example.com");
                    newUser.setFirstName("John");
                    newUser.setPassword("password");

                    userRepository.save(newUser);

                    return newUser;
                });
    }

}
