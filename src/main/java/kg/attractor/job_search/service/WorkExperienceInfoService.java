package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkExperienceInfoService {
    @Transactional
    void createWorkExperienceInfo(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Integer resumeId);

    void deleteWorkExperienceInfoByResumeId(Integer resumeId);

    List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Integer resumeId);
}
