package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.RecordAlreadyExistsException;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.service.ApplicationService;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final RespondedApplicantRepository respondedApplicantRepository;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        log.info("Starting response to vacancy: resumeId={}, vacancyId={}", resumeId, vacancyId);

        int vacancyCategory = vacancyService.getCategoryIdByVacancyId(vacancyId);
        int resumeCategory = resumeService.getCategoryIdByResumeId(resumeId);
        log.debug("Vacancy category: {}, Resume category: {}", vacancyCategory, resumeCategory);

        if (vacancyCategory != resumeCategory) {
            log.warn("Category mismatch: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new BadRequestException(messageSource.getMessage("error.category.mismatch", null, LocaleContextHolder.getLocale()));
        }

        if (respondedApplicantRepository.findByResumeIdAndVacancyId(resumeId, vacancyId).isPresent()) {
            log.warn("Duplicate application detected: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new RecordAlreadyExistsException(messageSource.getMessage("error.application.exists", null, LocaleContextHolder.getLocale()));
        }

        RespondedApplicant respondedApplicant = new RespondedApplicant();
        respondedApplicant.setResume(resumeService.getResumeModelById(resumeId));
        respondedApplicant.setVacancy(vacancyService.getVacancyModelById(vacancyId));
        respondedApplicant.setConfirmation(false);

        respondedApplicantRepository.save(respondedApplicant);

        log.info("Response successfully saved: {}", respondedApplicant);
        new RespondenApplicantDto();
        return RespondenApplicantDto.builder()
                .resumeId(resumeId)
                .vacancyId(vacancyId)
                .confirmation(false)
                .build();
    }
}
