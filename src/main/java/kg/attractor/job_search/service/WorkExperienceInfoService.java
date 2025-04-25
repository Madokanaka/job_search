package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkExperienceInfoService {
    @Transactional
    void createWorkExperienceInfo(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Resume resume);

    void deleteWorkExperienceInfoByResumeId(Integer resumeId);

    List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Integer resumeId);
}
