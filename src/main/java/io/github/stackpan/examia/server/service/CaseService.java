package io.github.stackpan.examia.server.service;

import io.github.stackpan.examia.server.entity.Case;
import io.github.stackpan.examia.server.http.request.CreateCaseRequest;
import io.github.stackpan.examia.server.http.request.UpdateCaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaseService {

    Page<Case> getAllByUserId(Pageable pageable, String userId);

    Case getByCaseIdAndUserId(String caseId, String userId);

    Case createByUserId(CreateCaseRequest data, String userId);

    void updateByCaseIdAndUserId(String caseId, String userId, UpdateCaseRequest model);

    void deleteByCaseIdAndUserId(String caseId, String userId);

}
