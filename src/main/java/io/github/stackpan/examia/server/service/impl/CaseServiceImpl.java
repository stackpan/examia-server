package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.repository.CaseRepository;
import io.github.stackpan.examia.server.repository.UserRepository;
import io.github.stackpan.examia.server.service.CaseService;
import io.github.stackpan.examia.server.util.UUIDs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;

    private final UserRepository userRepository;

    private final String resourceContextName = Case.class.getSimpleName();

    @Override
    public Page<Case> getAll(Pageable pageable) {
        return caseRepository.findAll(pageable);
    }

    @Override
    public Case getById(String id) {
        return findByIdOrThrow(id);
    }

    @Override
    @Transactional
    public Case create(CreateCaseRequest data) {
        var aCase = new Case();

        aCase.setTitle(data.title());
        aCase.setDescription(data.description());
        aCase.setDurationInSeconds(data.durationInSeconds());
//        aCase.setOwner(createUser());

        caseRepository.save(aCase);

        return aCase;
    }

    @Override
    @Transactional
    public void updateById(String id, CreateCaseRequest model) {
        var aCase = findByIdOrThrow(id);

        aCase.setTitle(model.title());
        aCase.setDescription(model.description());
        aCase.setDurationInSeconds(model.durationInSeconds());

        caseRepository.save(aCase);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        var caseUuid = validateUuid(id);

        if (!caseRepository.existsById(caseUuid)) {
            throw new ResourceNotFoundException(resourceContextName, id);
        }

        caseRepository.deleteById(caseUuid);
    }

    private UUID validateUuid(String id) {
        return UUIDs.fromString(id).orElseThrows(() -> new ResourceNotFoundException(resourceContextName, id));
    }

    private Case findByIdOrThrow(String id) {
        return caseRepository.findById(validateUuid(id))
                .orElseThrow(() -> new ResourceNotFoundException(resourceContextName, id));
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
