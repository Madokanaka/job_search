package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.IllegalYearsException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.repository.WorkExperienceInfoRepository;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {

    private final WorkExperienceInfoRepository workExperienceInfoRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void createWorkExperienceInfo(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Resume resume) {
        log.info("Creating work experience info for resumeId={}", resume.getId());
        if (workExperienceInfoDtoList == null || workExperienceInfoDtoList.isEmpty()) {
            log.info("Received null or empty workExperienceInfoDto list for resumeId={}", resume.getId());
            return;
        }

        validateWorkExperienceYears(workExperienceInfoDtoList, resume);

        for (WorkExperienceInfoDto workExperienceInfoDto : workExperienceInfoDtoList) {
            if (isEmptyWorkExperience(workExperienceInfoDto)) {
                log.debug("Skipping empty work experience entry for resumeId={}", resume.getId());
                continue;
            }


            log.debug("Processing workExperienceInfoDto: {}", workExperienceInfoDto);
            WorkExperienceInfo workExperienceInfo = WorkExperienceInfo.builder()
                    .resume(resume)
                    .years(workExperienceInfoDto.getYears())
                    .companyName(workExperienceInfoDto.getCompanyName())
                    .position(workExperienceInfoDto.getPosition())
                    .responsibilities(workExperienceInfoDto.getResponsibilities())
                    .build();
            workExperienceInfoRepository.save(workExperienceInfo);
            log.debug("Work experience info saved: {}", workExperienceInfo);
        }
    }

    private void validateWorkExperienceYears(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Resume resume) {
        Integer age = resume.getApplicant().getAge();

        int maxWorkYears = Math.max(0, age - 18);

        int totalWorkYears = workExperienceInfoDtoList.stream()
                .filter(dto -> !isEmptyWorkExperience(dto) && dto.getYears() != null)
                .mapToInt(WorkExperienceInfoDto::getYears)
                .sum();

        if (totalWorkYears > maxWorkYears) {
            log.error("Total work experience years {} exceeds maximum allowed {} for resumeId={}",
                    totalWorkYears, maxWorkYears, resume.getId());
            throw new IllegalYearsException(
                    "Total work experience years (" + totalWorkYears + ") cannot exceed " + maxWorkYears + " (age - 18)");
        }
    }


    private boolean isEmptyWorkExperience(WorkExperienceInfoDto dto) {
        return (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) &&
                (dto.getPosition() == null || dto.getPosition().isBlank()) &&
                (dto.getResponsibilities() == null || dto.getResponsibilities().isBlank()) &&
                dto.getYears() == null;
    }

    @Override
    @Transactional
    public void deleteWorkExperienceInfoByResumeId(Integer resumeId) {
        log.info("Deleting work experience info for resumeId={}", resumeId);

        workExperienceInfoRepository.deleteByResumeId(resumeId);
        log.info("Deleted work experience info for resumeId={}", resumeId);
    }

    @Override
    public List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Integer resumeId) {
        log.info("Fetching work experience info for resumeId={}", resumeId);
        List<WorkExperienceInfo> workExperienceInfoList = workExperienceInfoRepository.findByResumeId(resumeId);

        List<WorkExperienceInfoDto> result = workExperienceInfoList.stream()
                .map(workExperienceInfo -> new WorkExperienceInfoDto(
                        workExperienceInfo.getYears(),
                        workExperienceInfo.getCompanyName(),
                        workExperienceInfo.getPosition(),
                        workExperienceInfo.getResponsibilities()))
                .collect(Collectors.toList());
        log.debug("Fetched {} work experience records for resumeId={}", result.size(), resumeId);
        return result;
    }
}