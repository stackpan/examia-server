package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.http.request.UpdateCaseRequest;
import io.github.stackpan.examia.server.repository.CaseRepository;
import io.github.stackpan.examia.server.repository.UserRepository;
import io.github.stackpan.examia.server.service.CaseService;
import io.github.stackpan.examia.server.util.UUIDs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;

    private final UserRepository userRepository;

    @Override
    public Page<Case> getAllByUserId(Pageable pageable, String userId) {
        return caseRepository.findAllByUserId(
                UUIDs.fromString(userId).orElseThrowsResourceNotFound(User.class),
                pageable
        );
    }

    @Override
    public Case getByCaseIdAndUserId(String caseId, String userId) {
        return findOneOrThrow(caseId, userId);
    }

    @Override
    @Transactional
    public Case createByUserId(CreateCaseRequest data, String userId) {
        var user = userRepository.findById(UUIDs.fromString(userId)
                        .orElseThrowsResourceNotFound(User.class))
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

        var examiaCase = new Case();
        examiaCase.setTitle(data.title());
        examiaCase.setDescription(data.description());
        examiaCase.setDurationInSeconds(data.durationInSeconds());
        examiaCase.setUser(user);

        caseRepository.save(examiaCase);
        return examiaCase;
    }

    @Override
    @Transactional
    public void updateByCaseIdAndUserId(String caseId, String userId, UpdateCaseRequest model) {
        var examiaCase = findOneOrThrow(caseId, userId);
        examiaCase.setTitle(model.title());
        examiaCase.setDescription(model.description());
        examiaCase.setDurationInSeconds(model.durationInSeconds());

        caseRepository.save(examiaCase);
    }

    @Override
    @Transactional
    public void deleteByCaseIdAndUserId(String caseId, String userId) {
        var examiaCase = findOneOrThrow(caseId, userId);

        caseRepository.deleteById(examiaCase.getId());
    }

    private Case findOneOrThrow(String caseId, String userId) {
        return caseRepository.findByIdAndUserId(
                UUIDs.fromString(caseId).orElseThrowsResourceNotFound(Case.class),
                UUIDs.fromString(userId).orElseThrowsResourceNotFound(User.class)
        ).orElseThrow(() -> new ResourceNotFoundException(Case.class.getSimpleName(), caseId));
    }

}
