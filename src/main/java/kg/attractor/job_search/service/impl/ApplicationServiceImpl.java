package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ApplicationDao;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.RecordAlreadyExistsException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.service.ApplicationService;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final CategoryService categoryService;
    private final RespondedApplicantRepository respondedApplicantRepository;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @Override
    @Transactional
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        log.info("Starting response to vacancy: resumeId={}, vacancyId={}", resumeId, vacancyId);

        int vacancyCategory = categoryService.getCategoryIdByVacancyId(vacancyId);
        int resumeCategory = categoryService.getCategoryIdByResumeId(resumeId);
        log.debug("Vacancy category: {}, Resume category: {}", vacancyCategory, resumeCategory);

        if (vacancyCategory != resumeCategory) {
            log.warn("Category mismatch: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new BadRequestException("Vacancy's and resume's category are not the same");
        }

        if (respondedApplicantRepository.findByResumeIdAndVacancyId(resumeId, vacancyId).isPresent()) {
            log.warn("Duplicate application detected: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new RecordAlreadyExistsException("Application already exists");
        }

        RespondedApplicant respondedApplicant = new RespondedApplicant();
        respondedApplicant.setResume(resumeService.getResumeModelById(resumeId));
        respondedApplicant.setVacancy(vacancyService.getVacancyModelById(vacancyId));
        respondedApplicant.setConfirmation(false);

        log.info("Response successfully saved: {}", respondedApplicant);
        return new RespondenApplicantDto().builder()
                .resumeId(resumeId)
                .vacancyId(vacancyId)
                .confirmation(false)
                .build();
    }
}
