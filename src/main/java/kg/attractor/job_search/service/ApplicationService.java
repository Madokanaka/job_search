package kg.attractor.job_search.service;

import kg.attractor.job_search.model.RespondedApplicant;

import java.util.List;

public interface ApplicationService {
    RespondedApplicant respondToVacancy(Integer vacancyId, Integer resumeId);

    List<RespondedApplicant> getRespondedApplicants(Integer vacancyId);
}
