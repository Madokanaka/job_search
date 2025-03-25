package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ApplicationDao;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationDao applicationDao;

    @Override
    @Transactional
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        int vacancyCategory = applicationDao.getCategoryByVacancyId(vacancyId);
        int resumeCategory = applicationDao.getCategoryByResumeId(resumeId);
        if (vacancyCategory != resumeCategory) {
            throw new DatabaseOperationException("Vacancy's and resume's category are not the same");
        }
        if (applicationDao.findResponseByResumeAndVacancy(resumeId, vacancyId) != null) {
            throw new DatabaseOperationException("Application already exists");
        }
        RespondenApplicantDto response = applicationDao.saveResponse(resumeId, vacancyId);
        if (response == null) {
            throw new DatabaseOperationException("Failed to save response or response is null");
        }
        return response;
    }
}
