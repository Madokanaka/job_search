package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ApplicationDao;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationDao applicationDao;

    @Override
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        RespondenApplicantDto response = applicationDao.saveResponse(resumeId, vacancyId);
        if (response == null) {
            throw new DatabaseOperationException("Failed to save response or response is null");
        }
        return response;
    }
}
