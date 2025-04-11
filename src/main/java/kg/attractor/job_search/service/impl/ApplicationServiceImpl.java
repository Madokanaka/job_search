package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ApplicationDao;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.RecordAlreadyExistsException;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationDao applicationDao;

    @Override
    @Transactional
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        log.info("Starting response to vacancy: resumeId={}, vacancyId={}", resumeId, vacancyId);

        int vacancyCategory = applicationDao.getCategoryByVacancyId(vacancyId);
        int resumeCategory = applicationDao.getCategoryByResumeId(resumeId);
        log.debug("Vacancy category: {}, Resume category: {}", vacancyCategory, resumeCategory);

        if (vacancyCategory != resumeCategory) {
            log.warn("Category mismatch: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new BadRequestException("Vacancy's and resume's category are not the same");
        }

        if (applicationDao.findResponseByResumeAndVacancy(resumeId, vacancyId) != null) {
            log.warn("Duplicate application detected: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new RecordAlreadyExistsException("Application already exists");
        }

        RespondenApplicantDto response = applicationDao.saveResponse(resumeId, vacancyId);
        if (response == null) {
            log.error("Failed to save response: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new DatabaseOperationException("Failed to save response or response is null");
        }

        log.info("Response successfully saved: {}", response);
        return response;
    }
}
