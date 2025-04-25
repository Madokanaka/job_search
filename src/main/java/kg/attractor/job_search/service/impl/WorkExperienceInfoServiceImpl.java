package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.repository.WorkExperienceInfoRepository;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {

    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    @Override
    @Transactional
    public void createWorkExperienceInfo(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Resume resume) {
        log.info("Creating work experience info for resumeId={}", resume.getId());
        if (workExperienceInfoDtoList != null) {
            if (!workExperienceInfoDtoList.isEmpty()) {
                workExperienceInfoDtoList.forEach(workExperienceInfoDto -> {
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
                });
            } else {
                log.info("Received empty workExperienceInfoDto list for resumeId={}", resume.getId());
            }
        } else {
            log.info("Received null workExperienceInfoDto list for resumeId={}", resume.getId());
        }
    }

    @Override
    @Transactional
    public void deleteWorkExperienceInfoByResumeId(Integer resumeId) {
        log.info("Deleting work experience info for resumeId={}", resumeId);
        if (workExperienceInfoRepository.findByResumeId(resumeId).isEmpty()) {
            log.warn("Resume not found for deletion with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        workExperienceInfoRepository.deleteByResumeId(resumeId);
        log.info("Deleted work experience info for resumeId={}", resumeId);
    }

    @Override
    public List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Integer resumeId) {
        log.info("Fetching work experience info for resumeId={}", resumeId);
        List<WorkExperienceInfo> workExperienceInfoList = workExperienceInfoRepository.findByResumeId(resumeId);
        if (workExperienceInfoList.isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
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