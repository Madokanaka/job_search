package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.CategoryMismatchException;
import kg.attractor.job_search.exception.DuplicateApplicationException;
import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.repository.RespondedApplicantRepository;
import kg.attractor.job_search.service.ApplicationService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserService userService;

    @Override
    @Transactional
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        log.info("Starting response to vacancy: resumeId={}, vacancyId={}", resumeId, vacancyId);

        int vacancyCategory = vacancyService.getCategoryIdByVacancyId(vacancyId);
        int resumeCategory = resumeService.getCategoryIdByResumeId(resumeId);
        log.debug("Vacancy category: {}, Resume category: {}", vacancyCategory, resumeCategory);

        if (vacancyCategory != resumeCategory) {
            log.warn("Category mismatch: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new CategoryMismatchException(messageSource.getMessage("error.category.mismatch", null, LocaleContextHolder.getLocale()));
        }

        if (respondedApplicantRepository.findByResumeIdAndVacancyId(resumeId, vacancyId).isPresent()) {
            log.warn("Duplicate application detected: resumeId={}, vacancyId={}", resumeId, vacancyId);
            throw new DuplicateApplicationException(messageSource.getMessage("error.application.exists", null, LocaleContextHolder.getLocale()));
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

    @Override
    public Page<RespondenApplicantDto> getApplicationsByUser(org.springframework.security.core.userdetails.User principal, Pageable pageable) {
        User user = userService.findUserModelByEmail(principal.getUsername());

        Page<RespondedApplicant> applications;
        if (user.getAccountType().equalsIgnoreCase("applicant")) {
            applications = respondedApplicantRepository.findByResumeApplicantId(user.getId(), pageable);
        } else if (user.getAccountType().equalsIgnoreCase("employer")) {
            applications = respondedApplicantRepository.findByVacancyAuthorId(user.getId(), pageable);
        } else {
            applications = respondedApplicantRepository.findAll(pageable);
        }

        return applications.map(this::convertToDto);
    }

    private RespondenApplicantDto convertToDto(RespondedApplicant entity) {
        User applicant = entity.getResume().getApplicant();
        User employer = entity.getVacancy().getAuthor();


        return RespondenApplicantDto.builder()
                .id(entity.getId())
                .resumeId(entity.getResume().getId())
                .resumeName(entity.getResume().getName())
                .vacancyId(entity.getVacancy().getId())
                .vacancyName(entity.getVacancy().getName())
                .applicantId(applicant.getId())
                .applicantName(applicant.getName() + " " + applicant.getSurname())
                .employerId(employer.getId())
                .employerName(employer.getName() + " " + employer.getSurname())
                .confirmation(entity.getConfirmation())
                .build();
    }

    @Override
    public String getUserRole(org.springframework.security.core.userdetails.User principal) {
        UserDto user = userService.findUserByEmail(principal.getUsername()).get();
        return user.getAccountType().toLowerCase();
    }
}
