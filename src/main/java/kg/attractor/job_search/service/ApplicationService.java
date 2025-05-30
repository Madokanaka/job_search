package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId);

    Page<RespondenApplicantDto> getApplicationsByUser(org.springframework.security.core.userdetails.User principal, Pageable pageable);

    String getUserRole(org.springframework.security.core.userdetails.User principal);
}
