package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.model.RespondedApplicant;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId);

}
