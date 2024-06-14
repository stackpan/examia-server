package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.repository.CaseRepository;
import io.github.stackpan.examia.server.repository.UserRepository;
import io.github.stackpan.examia.server.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;

    private final UserRepository userRepository;

    @Override
    public Page<Case> getAll(Pageable pageable) {
        return caseRepository.findAll(pageable);
    }

    @Override
    public Case getById(UUID id) {
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
    public void updateById(UUID id, CreateCaseRequest model) {
        var aCase = findByIdOrThrow(id);

        aCase.setTitle(model.title());
        aCase.setDescription(model.description());
        aCase.setDurationInSeconds(model.durationInSeconds());

        caseRepository.save(aCase);
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
