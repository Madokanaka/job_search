package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.exception.IllegalEducationDatesException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationInfoServiceImpl implements EducationInfoService {

    private final EducationInfoRepository educationInfoRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void createEducationInfo(List<EducationInfoDto> educationInfoDtoList, Resume resume) {
        log.info("Creating education info for resumeId={}", resume.getId());
        if (educationInfoDtoList == null || educationInfoDtoList.isEmpty()) {
            log.info("Received null or empty educationInfoDto list for resumeId={}", resume.getId());
            return;
        }

        for (EducationInfoDto educationInfoDto : educationInfoDtoList) {
            if (isEmptyEducationInfo(educationInfoDto)) {
                log.debug("Skipping empty education entry for resumeId={}", resume.getId());
                continue;
            }

            validateDates(educationInfoDto, resume.getId());

            log.debug("Processing educationInfoDto: {}", educationInfoDto);
            EducationInfo educationInfo = new EducationInfo();
            educationInfo.setResume(resume);
            educationInfo.setInstitution(educationInfoDto.getInstitution());
            educationInfo.setDegree(educationInfoDto.getDegree());
            educationInfo.setProgram(educationInfoDto.getProgram());
            educationInfo.setEndDate(educationInfoDto.getEndDate());
            educationInfo.setStartDate(educationInfoDto.getStartDate());
            educationInfoRepository.save(educationInfo);
            log.debug("Education info saved: {}", educationInfo);
        }
    }

    private void validateDates(EducationInfoDto dto, Integer resumeId) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        if (startDate == null || endDate == null) {
            log.error("Start date or end date is null for resumeId={}", resumeId);
            throw new IllegalEducationDatesException(messageSource.getMessage("error.education.dates.null", null, LocaleContextHolder.getLocale()));
        }

        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.now().plusYears(1);

        if (startDate.isBefore(minDate) || startDate.isAfter(maxDate)) {
            log.error("Invalid start date {} for resumeId={}", startDate, resumeId);
            throw new IllegalEducationDatesException(messageSource.getMessage("error.education.startDate.invalid", new Object[]{minDate, maxDate.getYear()}, LocaleContextHolder.getLocale()));
        }
        if (endDate.isBefore(minDate) || endDate.isAfter(maxDate)) {
            log.error("Invalid end date {} for resumeId={}", endDate, resumeId);
            throw new IllegalEducationDatesException(messageSource.getMessage("error.education.endDate.invalid", new Object[]{minDate, maxDate.getYear()}, LocaleContextHolder.getLocale()));
        }
        if (endDate.isBefore(startDate)) {
            log.error("End date {} is before start date {} for resumeId={}", endDate, startDate, resumeId);
            throw new IllegalEducationDatesException(messageSource.getMessage("error.education.endBeforeStart", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    @Transactional
    public void deleteEducationInfoByResumeId(Integer resumeId) {
        log.info("Deleting education info for resumeId={}", resumeId);

        educationInfoRepository.deleteByResumeId(resumeId);
        log.info("Deleted education info for resumeId={}", resumeId);
    }

    @Override
    public List<EducationInfoDto> getEducationInfoByResumeId(Integer resumeId) {
        log.info("Fetching education info for resumeId={}", resumeId);
        List<EducationInfo> educationInfoList = educationInfoRepository.findByResumeId(resumeId);

        List<EducationInfoDto> result = educationInfoList.stream()
                .map(educationInfo -> new EducationInfoDto(
                        educationInfo.getInstitution(),
                        educationInfo.getProgram(),
                        educationInfo.getStartDate(),
                        educationInfo.getEndDate(),
                        educationInfo.getDegree()))
                .collect(Collectors.toList());
        log.debug("Fetched {} education records for resumeId={}", result.size(), resumeId);
        return result;
    }

    private boolean isEmptyEducationInfo(EducationInfoDto dto) {
        return (dto.getInstitution() == null || dto.getInstitution().isBlank()) &&
                (dto.getProgram() == null || dto.getProgram().isBlank()) &&
                (dto.getDegree() == null || dto.getDegree().isBlank()) &&
                dto.getStartDate() == null &&
                dto.getEndDate() == null;
    }
}
